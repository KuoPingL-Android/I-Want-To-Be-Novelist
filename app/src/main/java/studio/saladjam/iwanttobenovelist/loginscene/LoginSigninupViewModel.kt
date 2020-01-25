package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util.getString
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus


class LoginSigninupViewModel (private val repository: Repository): ViewModel() {

    private val _status = MutableLiveData<APILoadingStatus>()
    val loadingStatus: LiveData<APILoadingStatus>
        get() = _status

    val isLoading = MediatorLiveData<Boolean>().apply {
        addSource(loadingStatus){ value = loadingStatus.value == APILoadingStatus.LOADING }
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    /**LOGIN WITH FACEBOOK*/
    private val _loginWithFacebook = MutableLiveData<Boolean>()
    val loginWithFacebook: LiveData<Boolean>
        get() = _loginWithFacebook

    lateinit var fbCallBackManager: CallbackManager

    fun loginWithFacebookSelected() {
        _loginWithFacebook.value = true
    }

    fun doneLoggingInWithFacebook() {
        _loginWithFacebook.value = null
        loginFromFb()
    }

    private fun loginFromFb() {
        _status.value = APILoadingStatus.LOADING
        fbCallBackManager = CallbackManager.Factory.create()
        coroutineScope.launch {
            val result = repository.loginViaFacebook(fbCallBackManager)
            when(result) {
                is Result.Success -> {
                    if (result.data) {
                        _shouldNavigateToNextLoginPage.value = true
                    } else {
                        // Do nothing
                    }
                }

                is Result.Error -> {
                    _error.value = result.exception.toString()
                }

                is Result.Fail -> {
                    Logger.i("ERROR = ${result.error}")
                }
            }
        }
    }

    /**LOGIN WITH GOOGLE*/
    private val _loginWithGoogle = MutableLiveData<Boolean>()
    val loginWithGoogle: LiveData<Boolean>
        get() = _loginWithGoogle

    fun loginWithGoogleSelected() {
        _loginWithGoogle.value = true
    }

    fun doneLoggingInWithGoogle() {
        _loginWithGoogle.value = null
    }

    var mGoogleSignInClient: GoogleSignInClient? = null

    fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>) {
        val result = repository.handleGoogleTask(completedTask)
        when(result) {
            is Result.Success -> {
                _shouldNavigateToNextLoginPage.value = true
            }

            is Result.Fail -> {
                _error.value = result.error
            }

            is Result.Error -> {
                Logger.i("handleGoogleTask ERROR : ${result.exception.message}")
            }
        }
    }



    /**LOGIN as VISITOR*/
    fun loginAsVisitorSelected() {
        _shouldNavigateToHomePage.value = true
    }

    /**LOGIN with TOKEN*/
    fun loginWithUser(user: User) {

    }

    /**NAVIGATION*/
    // HOME_PAGE
    private val _shouldNavigateToHomePage = MutableLiveData<Boolean>()
    val shouldNavigateToHomePage: LiveData<Boolean>
        get() = _shouldNavigateToHomePage

    fun navigateToHomePage() {
        _shouldNavigateToHomePage.value = true
    }

    fun doneNavigateToHomePage() {
        _shouldNavigateToHomePage.value = null
    }

    // SELECT ROLE PAGE
    private val _shouldNavigateToNextLoginPage = MutableLiveData<Boolean>()
    val shouldNavigateToNextLoginPage: LiveData<Boolean>
        get() = _shouldNavigateToNextLoginPage

    fun navigateToNextLoginPage() {
        _shouldNavigateToNextLoginPage.value = true
    }

    fun doneNavigateToNextLoginPage() {
        _shouldNavigateToNextLoginPage.value = null
    }


}