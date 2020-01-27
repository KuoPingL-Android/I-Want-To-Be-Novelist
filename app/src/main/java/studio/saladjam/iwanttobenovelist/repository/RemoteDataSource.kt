package studio.saladjam.iwanttobenovelist.repository

import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object IWBNRemoteDataSource: Repository {

    override suspend fun getUser(token: String): Result<User> = suspendCoroutine {continuation ->
        if (!IWBNApplication.isNetworkConnected) {
            continuation.resume(Result.Fail("No network"))
        }

        IWBNApplication.container.userCollection.document(token).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)

                if (user != null) {
                    continuation.resume(Result.Success(user))
                } else {
                    continuation.resume(Result.Fail("Invalid User Token"))
                }

            }
            .addOnCanceledListener {
                continuation.resume(Result.Fail("Get User Cancelled"))
            }
            .addOnFailureListener {
                continuation.resume(Result.Error(it))
            }
    }

    override fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean> {
        return try {
            val result = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Logger.i("ACCOUNT = ${result}")

            val token = result?.idToken
            val id = result?.id
            val name = result?.displayName
            val email = result?.email

            if (result == null || id == null || email == null || name == null || token == null) {
                Result.Fail("GOOGLE RESULT is NULL")
            } else {
                IWBNApplication.user.apply {
                    this.token = token
                    this.googleToken = token
                    this.userID = id
                    this.name = name
                    this.email = email
                }
                Result.Success(true)
            }


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.w( "handleGoogleTask signInResult:failed code=" + e.statusCode)
            Result.Error(e)
        }

    }

    override suspend fun loginViaFacebook(
        callbackManager: CallbackManager): Result<Boolean> = suspendCoroutine {continuation ->
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(p0: LoginResult?) {
                p0?.let {result ->
                    IWBNApplication.user.fbToken = result.accessToken.token
                    IWBNApplication.user.token = result.accessToken.token

                    /**GET USER ID and EMAIL*/
                    val request = GraphRequest.newMeRequest(result.accessToken) {jsonObject, graphResponse ->
                        try {
                            val email = jsonObject.get("email")
                            val id = jsonObject.get("id")
                            val name = jsonObject.get("name")
                            IWBNApplication.user.apply {
                                userID = id as String
                                this.email = email as String
                                this.name = name as String
                            }

                            continuation.resume(Result.Success(true))

                        } catch (e: Exception) {
                            Logger.i("loginFromFb EXCEPTION=${e.message}")
                            continuation.resume(Result.Fail(e.toString()))
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id, name, email")
                    request.parameters = parameters
                    request.executeAsync()
                }
            }

            override fun onCancel() {
                continuation.resume(Result.Success(false))
            }

            override fun onError(p0: FacebookException?) {
                Logger.w("[${this::class.simpleName}] exception=${p0?.message}")

                p0?.message?.let {
                    if (it.contains("ERR_INTERNET_DISCONNECTED")) {
                        continuation.resume(Result.Fail(Util.getString(R.string.internet_not_connected)))
                    } else {
                        continuation.resume(Result.Fail(it))
                    }
                }
            }

        })
    }

    override suspend fun loginUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->
        val auth = IWBNApplication.container.auth
        if (user.fbToken != null) {

            val credential = FacebookAuthProvider.getCredential(user.fbToken!!)
            auth.signInWithCredential(credential).addOnCompleteListener {task ->
                when {
                    task.isSuccessful -> {
                        auth.currentUser?.let {firebaseUser ->
                            IWBNApplication.user.userID = firebaseUser.uid
                            IWBNApplication.container.userCollection.document(user.userID).set(user)
                                .addOnCompleteListener {task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {
                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail(task.exception!!.toString()))
                                    }
                                }
                        }
                    }

                    task.isCanceled -> {
                        Logger.i("FB SIGNIN CANCEL : ${task.exception}")
                        continuation.resume(Result.Fail("Cancelled"))
                    }

                    task.isComplete -> {
                        Logger.i("ERROR")
                        continuation.resume(Result.Error(task.exception!!))
                    }
                }
            }

        } else if (user.googleToken != null) {
            val credential = GoogleAuthProvider.getCredential(user.googleToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {task ->
                when {
                    task.isSuccessful -> {
                        auth.currentUser?.let {firebaseUser ->
                            IWBNApplication.user.userID = firebaseUser.uid
                            IWBNApplication.container.userCollection.document(user.userID).set(user)
                                .addOnCompleteListener {task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {
                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail(task.exception!!.toString()))
                                    }
                                }
                        }
                    }

                    task.isCanceled -> {
                        Logger.i("signInWithCredential:failure ${task.exception}")
                        continuation.resume(Result.Fail("Cancelled"))
                    }

                    task.isComplete -> {
                        Logger.i("ERROR")
                        continuation.resume(Result.Error(task.exception!!))
                    }
                }
            }
        }
    }

    override suspend fun getCategory(): Result<List<String>> = suspendCoroutine {continuation ->
        IWBNApplication.container.categoryCollection.document("chinese").get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    var list = mutableListOf<String>()
                    val result = (task.result?.get("list") as? List<String>)?.toMutableList()
                    if (result != null) {
                        list = result.toMutableList()
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
                    continuation.resume(Result.Fail("UNKNOWN"))
                }
            }
    }

}