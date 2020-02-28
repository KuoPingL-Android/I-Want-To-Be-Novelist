package studio.saladjam.iwanttobenovelist.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.dataclass.*
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters

interface Repository {
    suspend fun loginUser(user: User): Result<Boolean>
    suspend fun getCategory(): Result<Categories>
    suspend fun checkIfUserExist(user: User): Result<Boolean>
    suspend fun loginViaFacebook(callbackManager: CallbackManager): Result<Boolean>
    fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean>
    suspend fun getUser(token: String): Result<User>

    /** HOME PAGE */
    suspend fun getUserRecommendedList(user: User): Result<List<Book>>
    suspend fun getUserWork(user: User, limit: Long? = null): Result<List<Book>>
    suspend fun getUserFollowing(user: User): Result<List<Book>>
    fun addBooksFollowingSnapshotListener(userID: String, callback: (List<BookFollowee>) -> Unit)
    suspend fun getMostPopularBooks(): Result<List<Book>>

    /** CATEGORY */
    suspend fun getBooks(category: String, language: String = "zh", sortedBy: String = ""): Result<List<Book>>

    /** PROFILE PAGE */
    suspend fun getFollowingBooks(user: User): Result<List<Book>>

    /** CREATE BOOK */
    suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book>

    /**  FETCH BOOK CHAPTERS */
    suspend fun getChaptersIn(book: Book): Result<List<Chapter>>

    /** SAVING CHAPTER */
    suspend fun postChapter(chapter: Chapter): Result<Boolean>

    /** SAVING CHAPTER DETAILS */
    suspend fun postChapterWithDetails(chapter: Chapter,
                                       bitmapsMap: Map<String, Bitmap>,
                                       addOnCoordinators: List<ImageBlockRecorder>): Result<Boolean>

    /** READING PAGE */
    /** COMMENT */
    //TODO: ADD COMMENT REPOSITORY
    /** FOLLOW CHAPTER */
    //TODO: ADD FOLLOW CHAPTER REPOSITORY
    /** LIKE CHAPTER */
//    suspend fun updateLikeChapter(chapter: Chapter)

    /** GET A CHAPTER from BOOK */
    /**
     * @param Map<String, ImageBlockRecorder> :
     * this contains the File location of the Image
     * and the Actual view Location of the Image relative to the
     *
     * @param Chapter: the Chapter with the ChapterID in the book
     * */
    suspend fun getChapterWithDetails(chapterIndex: Int, book: Book): Result<Pair<Chapter, List<ImageBlockRecorder>>>

    ///TODO: GET RID OF THIS LIKES
    /** GET LIKES from BOOK */
    suspend fun getLikesForBook(book: Book): Result<List<String>>

    /** GET FOLLOWERS from BOOK */
    suspend fun getFollowersForBook(book: Book): Result<List<String>>

    /** FOLLOW the BOOK */
    suspend fun followBook(book: Book): Result<Boolean>
    suspend fun unfollowBook(book: Book): Result<Boolean>

    suspend fun getFollowingBooks(list: List<BookFollowee>): Result<List<Book>>

    /** SEARCH PAGE */
    suspend fun getBooksBasedOn(value: String = "", filter: SearchFilters = SearchFilters.POPULARITY): Result<List<Book>>
}