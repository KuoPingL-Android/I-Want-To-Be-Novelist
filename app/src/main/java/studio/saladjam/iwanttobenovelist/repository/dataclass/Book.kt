package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val bookID: String = "",
    val title: String = "",
    val subtitle: String = "",
    val authorID: String = "",
    val cover: String = "",
    val summary: String = "",
    val chapters: List<Chapter> = listOf(),
    val comments: List<Comment>? = null,
    val followers: List<String>? = null,
    val totalRecommendation: Long = 0L,
    val recommendationRecords: List<RecommendationRecord>? = null
): Parcelable

@Parcelize
data class RecommendationRecord (
    val totelRecommendation: Long = 0L,
    val dateString: String? = null
): Parcelable

@Parcelize
data class Chapter(
    val bookID: String = "",
    val chapterIndex: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val sections: List<Section>? = null
): Parcelable

@Parcelize
data class Section(
    val bookID: String = "",
    val chapterIndex: Int = 0,
    val sectionIndex: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val content: String = "",
    val images: Map<String, String>? = null,
    val comments: List<Comment>? = null
): Parcelable