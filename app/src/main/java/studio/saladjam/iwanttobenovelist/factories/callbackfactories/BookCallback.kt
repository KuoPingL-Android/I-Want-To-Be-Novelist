package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookCallback : DiffUtil.ItemCallback<Book>() {
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return  (oldItem.authorID == newItem.authorID) &&
                (oldItem.bookID == newItem.bookID) &&
                (oldItem.createdTime ==  newItem.createdTime) &&
                (oldItem.lastUpdatedTime == newItem.lastUpdatedTime)
    }

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem === newItem
    }
}