package studio.saladjam.iwanttobenovelist.searchscene

import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

enum class SearchFilters (val value: Int, val string: String) {
    POPULARITY(0, IWBNApplication.context.resources.getString(R.string.search_filter_popularity)),
    MOST_RECENTLY(1, IWBNApplication.context.resources.getString(R.string.search_filter_most_recent)),
    RECOMMENDED(2, IWBNApplication.context.resources.getString(R.string.search_filter_recommended)),
    COMPLETED(3, IWBNApplication.context.resources.getString( R.string.search_filter_completed)),
}

fun SearchFilters.getFirestoreSortingKey(): String {
    return when(this) {
        SearchFilters.POPULARITY -> Book.Companion.BookKeys.POPULARITY.string
        SearchFilters.MOST_RECENTLY -> Book.Companion.BookKeys.LASTUPDATEDTIME.string
        SearchFilters.COMPLETED -> Book.Companion.BookKeys.ISCOMPLETED.string
        SearchFilters.RECOMMENDED -> Book.Companion.BookKeys.POPULARITY.string
    }
}