package studio.saladjam.iwanttobenovelist.repository

import android.graphics.Bitmap
import android.net.Uri
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Categories
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

/**
 * This is a combination of Local and Remote Data Sources
 * */

class DataSource(private val localDataSource: Repository,
                 private val remoteDataSource: Repository): Repository {

    override suspend fun loginUser(user: User): Result<Boolean> {
        return remoteDataSource.loginUser(user)
    }

    override suspend fun getCategory(): Result<Categories> {
        return remoteDataSource.getCategory()
    }

    override suspend fun loginViaFacebook(callbackManager: CallbackManager): Result<Boolean> {
        return remoteDataSource.loginViaFacebook(callbackManager)
    }

    override fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean> {
        return remoteDataSource.handleGoogleTask(completedTask)
    }

    override suspend fun getUser(token: String): Result<User> {
        return remoteDataSource.getUser(token)
    }

    override suspend fun getUserFollowing(user: User): Result<List<Book>> {
        return remoteDataSource.getUserFollowing(user)
    }

    override suspend fun getUserRecommendedList(user: User): Result<List<Book>> {
        return remoteDataSource.getUserRecommendedList(user)
    }

    override suspend fun getUserWork(user: User, limit: Long?): Result<List<Book>> {
        return remoteDataSource.getUserWork(user)
    }

    override suspend fun getMostPopularBooks(): Result<List<Book>> {
        return remoteDataSource.getMostPopularBooks()
    }

    override suspend fun getBooks(
        category: String,
        language: String,
        sortedBy: String
    ): Result<List<Book>> {
        return remoteDataSource.getBooks(category, language, sortedBy)
    }

    override suspend fun getFollowingBooks(user: User): Result<List<Book>> {
        return remoteDataSource.getFollowingBooks(user)
    }

    override suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book> {
        return remoteDataSource.createBook(title, imageBitmap)
    }

    override suspend fun getChaptersIn(book: Book): Result<List<Chapter>> {
        return remoteDataSource.getChaptersIn(book)
    }
}