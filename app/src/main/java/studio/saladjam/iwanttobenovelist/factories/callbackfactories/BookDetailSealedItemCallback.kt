package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem

class BookDetailSealedItemCallback : DiffUtil.ItemCallback<BookDetailSealedItem>() {
    override fun areContentsTheSame(
        oldItem: BookDetailSealedItem,
        newItem: BookDetailSealedItem
    ): Boolean {
        if (oldItem is BookDetailSealedItem.Header && newItem is BookDetailSealedItem.Header) {
            return true
        } else if (oldItem is BookDetailSealedItem.Chapters && newItem is BookDetailSealedItem.Chapters) {
            return oldItem.chapter.chapterID == newItem.chapter.chapterID
        }

        return false
    }

    override fun areItemsTheSame(
        oldItem: BookDetailSealedItem,
        newItem: BookDetailSealedItem
    ): Boolean {
        if (oldItem is BookDetailSealedItem.Header && newItem is BookDetailSealedItem.Header) {
            return true
        } else if (oldItem is BookDetailSealedItem.Chapters && newItem is BookDetailSealedItem.Chapters) {
            return oldItem.chapter.chapterID == newItem.chapter.chapterID
        }

        return false
    }
}