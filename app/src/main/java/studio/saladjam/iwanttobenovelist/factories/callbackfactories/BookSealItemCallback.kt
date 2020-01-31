package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.bookscene.sealclass.BookSealItem

class BookSealItemCallback: DiffUtil.ItemCallback<BookSealItem>() {
    override fun areContentsTheSame(oldItem: BookSealItem, newItem: BookSealItem): Boolean {
        return if (oldItem is BookSealItem.BookItem && newItem is BookSealItem.BookItem) {
            oldItem.chapter.chapterIndex == newItem.chapter.chapterIndex
        } else {
            (oldItem is BookSealItem.Header && newItem is BookSealItem.Header)
        }
    }

    override fun areItemsTheSame(oldItem: BookSealItem, newItem: BookSealItem): Boolean {
        return if (oldItem is BookSealItem.BookItem && newItem is BookSealItem.BookItem) {
            oldItem.chapter === newItem.chapter
        } else {
            (oldItem is BookSealItem.Header && newItem is BookSealItem.Header)
        }
    }
}