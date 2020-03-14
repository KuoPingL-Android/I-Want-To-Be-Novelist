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

    fun getDisplayName(language: String = "zh", id: String): String {
        return when(language) {
            "zh" -> genres.first { it.id == id }.zh
            "en" -> genres.first { it.id == id }.en
            else -> id
        }
    }
}

@Parcelize
data class Genre (
    val id: String = "",
    val zh: String = "",
    val en: String = ""
): Parcelable

