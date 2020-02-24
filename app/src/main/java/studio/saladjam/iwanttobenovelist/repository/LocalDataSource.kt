package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.repository.dataclass.*
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters

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

    override suspend fun getUserWork(user: User, limit: Long?): Result<List<Book>> {
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

    override suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getChaptersIn(book: Book): Result<List<Chapter>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postChapter(chapter: Chapter): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postChapterWithDetails(
        chapter: Chapter,
        bitmapsMap: Map<String, Bitmap>,
        addOnCoordinators: List<ImageBlockRecorder>
    ): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getChapterWithDetails(
        chapterIndex: Int,
        book: Book
    ): Result<Pair<Chapter, List<ImageBlockRecorder>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLikesForBook(book: Book): Result<List<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateFollowBook(book: Book): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getIsFollowedBook(book: Book): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addBooksFollowingSnapshotListener(
        userID: String,
        callback: (List<BookFollowee>) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFollowingBooks(list: List<BookFollowee>): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getBooksBasedOn(value: String, filter: SearchFilters): Result<List<Book>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}