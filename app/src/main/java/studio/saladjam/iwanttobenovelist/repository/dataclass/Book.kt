package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.model.value.ReferenceValue
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Book(
    val bookID: String = "",
    var title: String = "",
    var subtitle: String = "",
    val authorID: String = "",
    val authorName: String = "",
    var cover: String = "",
    var summary: String = "",
    var latestChapterID: String = "",
    val chapterCount: Int = 0,
    val comments: List<Comment> = listOf(),
    val followers: List<Follower> = listOf(),
    val popularity: Long = 0L,
    val totalRecommendation: Long = 0L,
    val recommendationRecords: List<RecommendationRecord>? = null,
    val createdTime: Long = Calendar.getInstance().timeInMillis,
    val lastUpdatedTime: Long = Calendar.getInstance().timeInMillis,
    val language: String = "zh",
    val category: String = "未知",
    var isOpenToPublic: Boolean = false
): Parcelable {
    companion object {
        enum class BookKeys(val string: String) {
            ID("bookID"),
            TITLE("title"),
            AUTHORID("authorID"),
            CREATEDTIME("createdTime"),
            LASTUPDATEDTIME("lastUpdatedTime"),
            LANGUAGE("language"),
            POPULARITY("popularity"),
            CHAPTERCOUNT("chapterCount"),
            LATESTCHAPTERID("latestChapterID")
        }
    }
}

@Parcelize
data class Follower(
    val userID: String = "",
    val createdDate: String = ""
): Parcelable

@Parcelize
data class RecommendationRecord (
    val total: Long = 0L,
    val recommendedDate: String? = null
): Parcelable

@Parcelize
data class Chapter(
    val bookID: String = "",
    var chapterID: String = "",
    val chapterIndex: Int = 0,
    var title: String = "",
    val subtitle: String = "",
    val updatedTime: Long = 0L,
    var images: List<String> = listOf(),
    var text: String = "",
    var popularity: Long = 0L,
    var isOpenToPublic: Boolean = false,
    var content: String = ""
): Parcelable {
    companion object {
        enum class ChapterKeys(val string: String) {
            BOOKID("bookID"),
            INDEX("chapterIndex"),
            TITLE("title"),
            SUBTITLE("subtitle"),
            POPULARITY("popularity"),
            CHAPTERID("chapterID"),
            ISOPENTOPUBLIC("isOpenToPublic"),
            UPDATEDTIME("updatedTime"),
            TEXT("text")
        }
    }
}

@Parcelize
data class Section(
    val bookID: String = "",
    val chapterID: String = "",
    val sectionID: String = "",
    val sectionIndex: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val content: String = "",
    val images: Map<String, String>? = null,
    val text: String = "",
    val comments: List<Comment>? = null,
    val popularity: Long = 0L
): Parcelable