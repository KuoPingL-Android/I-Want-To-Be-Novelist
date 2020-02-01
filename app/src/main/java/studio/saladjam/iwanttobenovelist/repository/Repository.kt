package studio.saladjam.iwanttobenovelist.repository

import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

interface Repository {
    suspend fun loginUser(user: User): Result<Boolean>
    suspend fun getCategory(): Result<List<String>>
    suspend fun loginViaFacebook(callbackManager: CallbackManager): Result<Boolean>
    fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean>
    suspend fun getUser(token: String): Result<User>

    /** HOME PAGE */
    suspend fun getUserRecommendedList(user: User): Result<List<Book>>
    suspend fun getUserWork(user: User): Result<List<Book>>
    suspend fun getUserFollowing(user: User): Result<List<Book>>
    suspend fun getMostPopularBooks(): Result<List<Book>>

    /** CATEGORY */
    suspend fun getBooks(category: String, language: String, sortedBy: String): Result<List<Book>>
}