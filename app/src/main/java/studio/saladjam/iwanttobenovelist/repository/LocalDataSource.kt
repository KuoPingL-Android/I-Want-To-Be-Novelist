package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Categories
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

class IWBNLocalDataSource(private val context: Context): Repository {

    override fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loginViaFacebook(callbackManager: CallbackManager): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loginUser(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCategory(): Result<Categories> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUser(token: String):Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserFollowing(user: User): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserRecommendedList(user: User): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserWork(user: User): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getMostPopularBooks(): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getBooks(
        category: String,
        language: String,
        sortedBy: String
    ): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFollowingBooks(user: User): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getWorkingBooks(user: User): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}