package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R

@Parcelize
data class Categories(
    val genres: List<Genre> = listOf()
): Parcelable {
    fun getDisplayNames(language: String = "zh"): List<String> {
        return genres.map { it.zh }
    }

    fun getGenreIDFor(string: String): String {

        val result = genres.filter { it.zh == string }

        return if (result.isEmpty()) IWBNApplication.instance.getString(R.string.book_default_category)

        else result.first().id
    }
}

@Parcelize
data class Genre (
    val id: String = "",
    val zh: String = "",
    val en: String = ""
): Parcelable

