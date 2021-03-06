package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

class HomeSealItemCallback : DiffUtil.ItemCallback<HomeSealItems>() {
    override fun areItemsTheSame(oldItem: HomeSealItems, newItem: HomeSealItems): Boolean {
        if(oldItem is HomeSealItems.General && newItem is HomeSealItems.General) {

            val sortedOldBooks = oldItem.books.sortedBy { it.bookID }
            val sortedNewBooks = newItem.books.sortedBy { it.bookID }

            return sortedNewBooks.containsAll(sortedOldBooks) && sortedOldBooks.containsAll(sortedNewBooks)

        } else if (oldItem is HomeSealItems.CurrentReading && newItem is HomeSealItems.CurrentReading) {

            val sortedOldBooks = oldItem.books.sortedBy { it.bookID }
            val sortedNewBooks = newItem.books.sortedBy { it.bookID }

            return sortedNewBooks.containsAll(sortedOldBooks) && sortedOldBooks.containsAll(sortedNewBooks)

        } else if (oldItem is HomeSealItems.WorkInProgress && newItem is HomeSealItems.WorkInProgress) {

            val sortedOldBooks = oldItem.books.sortedBy { it.bookID }
            val sortedNewBooks = newItem.books.sortedBy { it.bookID }

            return sortedNewBooks.containsAll(sortedOldBooks) && sortedOldBooks.containsAll(sortedNewBooks)
        } else if (oldItem is HomeSealItems.Recommend && newItem is HomeSealItems.Recommend) {

            val sortedOldBooks = oldItem.books.sortedBy { it.bookID }
            val sortedNewBooks = newItem.books.sortedBy { it.bookID }

            return sortedNewBooks.containsAll(sortedOldBooks) && sortedOldBooks.containsAll(sortedNewBooks)
        }
        return false
    }

    override fun areContentsTheSame(oldItem: HomeSealItems, newItem: HomeSealItems): Boolean {
        if(oldItem is HomeSealItems.General && newItem is HomeSealItems.General) {
            return oldItem.books == newItem.books
        } else if (oldItem is HomeSealItems.CurrentReading && newItem is HomeSealItems.CurrentReading) {
            return oldItem.books == newItem.books
        } else if (oldItem is HomeSealItems.WorkInProgress && newItem is HomeSealItems.WorkInProgress) {
            return oldItem.books == newItem.books
        } else if (oldItem is HomeSealItems.Recommend && newItem is HomeSealItems.Recommend) {
            return oldItem.books == newItem.books
        }
        return false
    }

}