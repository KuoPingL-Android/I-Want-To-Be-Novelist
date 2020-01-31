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
    val createdTime: String = "",
    val popularity: Long = 0L,

    val agreedUser: List<Responder> = listOf(),
    val disagreedUser: List<Responder>? = listOf(),
    val bsUserIDs: List<Responder> = listOf(),
    val replies: List<Comment>? = null
): Parcelable

@Parcelize
data class Responder(
    val userID: String = "",
    val createdDate: String = ""
): Parcelable