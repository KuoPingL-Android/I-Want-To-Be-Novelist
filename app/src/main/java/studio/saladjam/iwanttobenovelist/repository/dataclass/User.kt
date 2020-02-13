package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

enum class Roles(val value: Int) {
    REVIEWER(0), WRITER(1)
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
    var preferredLanguage: String = "zh",
    /**COMMENTs*/
    val comments: List<UserComment> = listOf(),
    /**BOOKS*/
    val workedBooks: List<BookCollection> = listOf(),
    val recommendedBooks: List<BookCollection> = listOf(), // BookID
    val favoriteBooks: List<BookCollection> = listOf(), // BookID
    /**FOLLOWERS*/
    val followers: List<Follower> = listOf(), // UserID + createdTime
    val popularity: Long = 0L,
    val authorFollowees: List<Followee> = listOf(), // UserID + createdTime
    val commenterFollowees: List<Followee> = listOf(), // UserID + createdTime
    val bookFollowees: List<BookFollowee> = listOf() // BookID + createdTime
): Parcelable

@Parcelize
data class BookCollection(
    val bookID: String = "",
    val lastSeenDate: Timestamp = Timestamp(Calendar.getInstance().time),
    val lastUpdatedDate: Timestamp = Timestamp(Calendar.getInstance().time)
): Parcelable

@Parcelize
data class Followee(
    val userID: String = "",
    val createdDate: String = ""
): Parcelable

@Parcelize
data class BookFollowee(
    val bookID: String = "",
    val createdDate: String = ""
): Parcelable

@Parcelize
data class UserComment(
    val commentID: String = "",
    val createdTime: String = ""
): Parcelable