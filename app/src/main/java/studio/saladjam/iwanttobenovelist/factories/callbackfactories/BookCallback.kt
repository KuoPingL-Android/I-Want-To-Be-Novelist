package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookCallback : DiffUtil.ItemCallback<Book>() {
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return  (oldItem.authorID == newItem.authorID) &&
                (oldItem.bookID == newItem.bookID)
    }

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return  (oldItem.authorID == newItem.authorID) &&
                (oldItem.bookID == newItem.bookID)
    }
}