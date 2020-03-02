package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Categories(
    val genres: List<Genre> = listOf()
): Parcelable {
    fun getListFor(language: String = "zh"): List<String> {
        return genres.map { it.zh }
    }

    fun getGenreIDFor(string: String): String {

        val result = genres.filter { it.zh == string }

        return if (result.isEmpty()) "未知"

        else result.first().id
    }
}

@Parcelize
data class Genre (
    val id: String = "",
    val zh: String = "",
    val en: String = ""
): Parcelable

