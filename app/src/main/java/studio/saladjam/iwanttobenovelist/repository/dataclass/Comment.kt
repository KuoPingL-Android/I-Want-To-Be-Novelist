package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val commentID: String = "",
    val userID: String = "",
    val bookID: String = "",
    val section: Section? = null,
    val comment: String = "",
    val sourceCommentID: String = "",

    val agreedUserIDs: List<String>? = null,
    val disagreedUserIDs: List<String>? = null,
    val bsUserIDs: List<String>? = null,
    val replies: List<Comment>? = null
): Parcelable