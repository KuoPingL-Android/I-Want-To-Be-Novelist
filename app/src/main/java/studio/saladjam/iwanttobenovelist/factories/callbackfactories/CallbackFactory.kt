package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters

@Suppress("UNCHECKED_CAST")
class CallbackFactory : DiffItemCallbackFactoryInterface {

    override fun <T : DiffUtil.ItemCallback<R>?, R:Any> create(modelClass: Class<R>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(Genre::class.java)
                        -> GenreCallback()
                isAssignableFrom(String::class.java)
                        -> StringCallback()
                isAssignableFrom(Book::class.java)
                        -> BookCallback()
                isAssignableFrom(HomeSealItems::class.java)
                        -> HomeSealItemCallback()
                isAssignableFrom(BookDetailSealedItem::class.java)
                        -> BookDetailSealedItemCallback()
                isAssignableFrom(SearchFilters::class.java)
                        -> SearchFiltersCallback()
                isAssignableFrom(Chapter::class.java)
                        -> ChapterCallback()
                else -> IllegalArgumentException(
                    IWBNApplication.instance
                        .getString(
                            studio.saladjam.iwanttobenovelist.R.string.exception_unrecognized_class,
                            this::class.java, modelClass::class.java))
            }
        } as T
    }
}