package studio.saladjam.iwanttobenovelist.repository

import android.graphics.Bitmap
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.repository.dataclass.*

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

    override suspend fun postChapter(chapter: Chapter): Result<Boolean> {
        return remoteDataSource.postChapter(chapter)
    }

    override suspend fun postChapterWithDetails(
        chapter: Chapter,
        bitmapsMap: Map<String, Bitmap>,
        addOnCoordinators: List<ImageBlockRecorder>
    ): Result<Boolean> {
        return remoteDataSource.postChapterWithDetails(chapter, bitmapsMap, addOnCoordinators)
    }

    override suspend fun getChapterWithDetails(
        chapterIndex: Int,
        book: Book
    ): Result<Pair<Chapter, List<ImageBlockRecorder>>> {
        return remoteDataSource.getChapterWithDetails(chapterIndex, book)
    }

    override suspend fun getLikesForBook(book: Book): Result<List<String>> {
        return remoteDataSource.getLikesForBook(book)
    }

}