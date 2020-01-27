package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Roles(val value: Int) {
    COMMENTER(0), WRITER(1)
}

@Parcelize
data class User(
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var role: Int = 0, // default is commenter
    var token: String? = null,
    var fbToken: String? = null,
    var googleToken: String? = null,
    var preferredCategories: List<String> = listOf(),
    /**COMMENTs*/
    val comments: List<Comment>? = null,
    val agreedCommentIDs: List<Comment>? = null,
    val disagreedCommentIDs: List<Comment>? = null,
    /**BOOKS*/
    val books: List<Book>? = null,
    val recommendedBooks: List<String>? = null, // BookID
    val favoriteBooks: List<String>? = null, // BookID
    /**FOLLOWERS*/
    val followers: List<String>? = null, // UserID
    val authorFollowees: List<String>? = null, // UserID
    val commenterFollowees: List<String>? = null, // UserID
    val bookFollowees: List<String>? = null // BookID
): Parcelable