package studio.saladjam.iwanttobenovelist.repository

import android.graphics.Bitmap
import android.net.Uri
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
import com.google.firebase.firestore.Query
import kotlinx.coroutines.coroutineScope
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util
import studio.saladjam.iwanttobenovelist.repository.dataclass.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object IWBNRemoteDataSource: Repository {

    /** ADD DETAIL of the CHAPTER */
    override suspend fun postChapterWithDetails(
        chapter: Chapter,
        bitmapsMap: Map<String, Bitmap>,
        addOnCoordinators: List<ImageBlockRecorder>): Result<Boolean> = suspendCoroutine { continuation ->
        // SAVE ALL THE IMAGES FIRST
        val bookID = chapter.bookID
        val storageRef = IWBNApplication.container.fireStorage.reference
        var numberOfImageUploaded = 0

        for ((id, bitmap) in bitmapsMap) {

            val byteArrayOutput = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutput)
            val bytes = byteArrayOutput.toByteArray()

            storageRef.child(bookID + "/${id}.jpg").putBytes(bytes)
                .addOnSuccessListener {
                    numberOfImageUploaded += 1

                    if (numberOfImageUploaded == bitmapsMap.keys.count()) {
                        // UPDATE CHAPTER
                        chapter.images = bitmapsMap.keys.toList()

                        if (chapter.chapterID.isEmpty()) {
                            chapter.chapterID = IWBNApplication.container
                                .getChaptersRefFrom(bookID)
                                .document().id
                        }

                        IWBNApplication.container
                            .getChaptersRefFrom(bookID)
                            .document(chapter.chapterID)
                            .set(chapter)
                            .addOnSuccessListener {

                                // UPLOAD COORDINATES
                                var j = 0

                                for (coordinate in addOnCoordinators) {
                                    IWBNApplication.container
                                        .getChaptersRefFrom(bookID)
                                        .document(chapter.chapterID)
                                        .collection("coordinates")
                                        .document(coordinate.imageID)
                                        .set(coordinate)
                                        .addOnSuccessListener {

                                            j+=1

                                            if(j == addOnCoordinators.count()) {
                                                continuation.resume(Result.Success(true))
                                            }

                                        }
                                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                                }


                            }
                            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                            .addOnFailureListener { continuation.resume(Result.Error(it)) }

                    }
                }
                .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        }



    }

    /** ADD a CHAPTER to the book */
    override suspend fun postChapter(chapter: Chapter): Result<Boolean> = suspendCoroutine { continuation ->

        var chapterID = chapter.chapterID
        val bookID = chapter.bookID
        val chapterIndex = chapter.chapterIndex

        val isNewChapter = chapterID.isEmpty()

        if (chapterID.isEmpty()) chapterID = IWBNApplication.container.getChaptersRefFrom(chapter.bookID).document().id

        chapter.chapterID = chapterID

        IWBNApplication.container
            .getChaptersRefFrom(bookID)
            .document(chapterID)
            .set(chapter)
            .addOnSuccessListener {

                if (isNewChapter) {
                    IWBNApplication.container.bookCollection.document(bookID)
                        .update(Book.Companion.BookKeys.CHAPTERCOUNT.string, chapterIndex + 1)
                        .addOnSuccessListener { continuation.resume(Result.Success(true)) }
                        .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                } else {
                    continuation.resume(Result.Success(true))
                }
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** GET CHAPTERs FROM BOOK */
    override suspend fun getChaptersIn(book: Book): Result<List<Chapter>> = suspendCoroutine { continuation ->
        IWBNApplication.container.getChaptersRefFrom(book.bookID)
            .orderBy(Chapter.Companion.ChapterKeys.INDEX.string, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                continuation.resume(Result.Success(it.toObjects(Chapter::class.java)))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getChaptersIn : Canceled")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** CREATE BOOK */
    override suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book> = suspendCoroutine { continuation ->
        val bookDoc = IWBNApplication.container.bookCollection.document()

        // GET BOOK ID
        val bookID = bookDoc.id

        val storageRef = IWBNApplication.container.fireStorage.reference
        val byteArrayOutput = ByteArrayOutputStream()

        Logger.i("imageBitmap.byteCount=${imageBitmap.byteCount}")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutput)
        val bytes = byteArrayOutput.toByteArray()

        val uploadTask = storageRef.child(bookID + "/cover.jpg").putBytes(bytes)

        uploadTask
            .addOnSuccessListener {
                Logger.i("${it.metadata}")

                if (it.metadata != null &&
                    it.metadata?.path != null && it?.metadata?.bucket != null) {

                    val book = Book(
                        bookID,
                        title,
                        authorID = IWBNApplication.user.userID,
                        authorName = IWBNApplication.user.name,
                        createdTime = it.metadata?.creationTimeMillis ?: Calendar.getInstance().timeInMillis,
                        cover = "gs://" + it.metadata!!.bucket + "/" + it.metadata!!.path)

                    IWBNApplication.container.bookCollection.document(bookID).set(book)
                        .addOnSuccessListener {
                            // UPDATE USER WORK BOOK AS WELL
                            val bookRef = {
                                "ref" to IWBNApplication.container.bookCollection.document(bookID)
                            }

                            IWBNApplication.container.getWrittenBookRefFrom(IWBNApplication.user.userID)
                                .document(bookID).set(bookRef)
                                .addOnSuccessListener {
                                    continuation.resume(Result.Success(book))
                                }
                                .addOnCanceledListener { Result.Fail("CANCELED") }
                                .addOnFailureListener { continuation.resume(Result.Error(it)) }
                            Result.Success(book) }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }


                } else {
                    continuation.resume(Result.Fail("ERROR in METADATA"))
                }

            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener{ continuation.resume(Result.Error(it)) }
    }

    /** PROFILE PAGE */
    override suspend fun getFollowingBooks(user: User): Result<List<Book>>
            = suspendCoroutine {continuation ->

        val bookIDs = user.favoriteBooks.sortedBy { it.lastSeenDate }.map { it.bookID }.reversed()

        if (bookIDs.isEmpty()) {
            continuation.resume(Result.Fail("No bookID found"))
            return@suspendCoroutine
        }

        IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
            .addOnSuccessListener {
                val books = it.toObjects(Book::class.java)
                continuation.resume(Result.Success(books))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    private suspend fun getBooksFromIDs(bookIDs: List<String>): Result<List<Book>> = suspendCoroutine { continuation ->
        IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
            .addOnSuccessListener {
                val books = it.toObjects(Book::class.java)
                continuation.resume(Result.Success(books))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** CATEGORY PAGE */
    override suspend fun getBooks(category: String,
                                  language: String,
                                  sortedBy: String): Result<List<Book>>
            = suspendCoroutine { continuation ->

        var bookLanguage = language

        if (bookLanguage.isEmpty()) {
            bookLanguage = "zh" //TODO: CHANGE LANGUAGE TO DYNAMIC
        }

        if (category.isEmpty()) {
            // Get All Books on Specific Language <- Default User Language
            IWBNApplication.container.bookCollection
//                .whereEqualTo("language", language)
                .orderBy("createdTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                }
                .addOnCanceledListener {
                    continuation.resume(Result.Fail("CANCELED"))
                }
                .addOnFailureListener{
                    continuation.resume(Result.Error(it))
                }
        } else {
            IWBNApplication.container.bookCollection
//                .whereEqualTo("language", language)
//                .whereEqualTo("category", category)
                .orderBy("createdTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                }
                .addOnCanceledListener {
                    continuation.resume(Result.Fail("CANCELED"))
                }
                .addOnFailureListener{
                    continuation.resume(Result.Error(it))
                }
        }
    }



    /** HOME DATA */
    override suspend fun getMostPopularBooks(): Result<List<Book>> = suspendCoroutine { continuation ->
        IWBNApplication.container.bookCollection.orderBy("popularity", Query.Direction.DESCENDING).limit(20).get()
            .addOnSuccessListener {snapShot ->
                continuation.resume(Result.Success(snapShot.toObjects(Book::class.java)))
            }
            .addOnFailureListener {exception ->
                continuation.resume(Result.Error(exception))
            }
            .addOnCanceledListener {
                continuation.resume(Result.Fail("CANCELED"))
            }
    }

    override suspend fun getUserFollowing(user: User): Result<List<Book>> = suspendCoroutine {continuation ->
        if (user.bookFollowees.isEmpty()) {
            continuation.resume(Result.Success(listOf()))
        } else {
            val bookIDs = user.bookFollowees
            IWBNApplication.container.bookCollection.whereArrayContains("bookID", bookIDs).get()
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val results = task.result?.toObjects(Book::class.java)
                        if (results != null) continuation.resume(Result.Success(results))
                        else continuation.resume(Result.Success(listOf()))
                    } else if (task.isCanceled) {
                        continuation.resume(Result.Fail("CANCELED"))
                    } else if (task.exception != null) {
                        continuation.resume(Result.Error(task.exception!!))
                    } else
                    {
                        continuation.resume(Result.Fail("UNKNOWN"))
                    }
                }
        }
    }

    override suspend fun getUserRecommendedList(user: User): Result<List<Book>> = suspendCoroutine {continuation ->
        /** FETCH Recommended List */
        IWBNApplication.container.recommendation.get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val results = task.result?.toObjects(Book::class.java)
                    if (results != null) {
                        val bookIDs = results.map { it.bookID }

                        if (bookIDs.isEmpty()) {
                            continuation.resume(Result.Success(results))
                            return@addOnCompleteListener
                        }

                        /** FETCH Books from the BookID within the List */
                        IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
                            .addOnCompleteListener { bookTask ->
                                if (bookTask.isSuccessful) {
                                    val result = bookTask.result?.toObjects(Book::class.java)
                                    if (result != null) {continuation.resume(Result.Success(result))}
                                    else {continuation.resume(Result.Fail("getUserRecommendedList : FAILED CONVERT"))}

                                } else if (bookTask.isCanceled) {
                                    continuation.resume(Result.Fail("getUserRecommendedList : CANCELED"))
                                } else if (bookTask.exception != null) {
                                    continuation.resume(Result.Error(bookTask.exception!!))
                                } else {
                                    continuation.resume(Result.Fail("getUserRecommendedList : UNKNOWN ERROR"))
                                }
                            }
                    }
                    else continuation.resume(Result.Success(listOf()))
                } else if (task.isCanceled) {
                    continuation.resume(Result.Fail("CANCELED"))
                } else if (task.exception != null) {
                    continuation.resume(Result.Error(task.exception!!))
                } else
                {
                    continuation.resume(Result.Fail("UNKNOWN"))
                }
            }
    }

    override suspend fun getUserWork(user: User, limit: Long?): Result<List<Book>> = suspendCoroutine {continuation ->

        var ref = IWBNApplication.container.getWrittenBookRefFrom(user.userID)

        if (limit == null) {
            ref.get()
                .addOnSuccessListener {

                    val bookIDs = it.documents.map { it.id }

                    if (bookIDs.isEmpty()) {
                        continuation.resume(Result.Success(listOf()))
                        return@addOnSuccessListener
                    }

                    val numberOfPatches = bookIDs.size % 10
                    var i = 0
                    val allbooks = mutableListOf<Book>()

                    for (j in 0 until numberOfPatches) {

                        val beginning = j * 10
                        var ending = (j + 1) * 10 - 1

                        if (ending > bookIDs.size) {
                            ending = bookIDs.size
                        }

                        val sectionBookIDs = bookIDs.subList(beginning, ending)

                        IWBNApplication.container.bookCollection.whereIn("bookID", sectionBookIDs).get()
                            .addOnSuccessListener {
                                val books = it.toObjects(Book::class.java)
                                allbooks.addAll(books)

                                if (ending == bookIDs.count())
                                {
                                    allbooks.sortBy { it.createdTime }
                                    allbooks.reverse()
                                    continuation.resume(Result.Success(allbooks))
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                            .addOnFailureListener { continuation.resume(Result.Error(it)) }
                    }


                }
                .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        } else {
            ref.limit(limit).get()
                .addOnSuccessListener {

                    val bookIDs = it.documents.map { it.id }

                    if (bookIDs.isEmpty()) {
                        continuation.resume(Result.Success(listOf()))
                        return@addOnSuccessListener
                    }

                    IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
                        .addOnSuccessListener {
                            val books = it.toObjects(Book::class.java)

                            books.sortBy { it.createdTime }
                            books.reverse()

                            continuation.resume(Result.Success(books))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                }
                .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        }
    }

    /** LOGIN */
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

    override suspend fun getCategory(): Result<Categories> = suspendCoroutine {continuation ->
        IWBNApplication.container.categoryCollection.get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    var list = mutableListOf<Genre>()

                    val result = task.result
                    if (result != null) {
                        list = result.documents.map {
                            Genre(it.id,
                                zh = it.get("zh") as String,
                                en = it.get("en") as String)
                        }.toMutableList()
                    }
                    continuation.resume(Result.Success(Categories(list)))
                } else {
                    if (task.exception != null) {
                        val exception = task.exception!!
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${exception.message}")
                        continuation.resume(Result.Error(exception))
                    } else {
                        continuation.resume(Result.Fail("UNKNOWN"))
                    }
                }
            }
    }

}