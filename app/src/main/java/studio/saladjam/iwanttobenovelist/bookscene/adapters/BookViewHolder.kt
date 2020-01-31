package studio.saladjam.iwanttobenovelist.bookscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeBooksBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookViewHolder(val binding: ItemHomeBooksBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book) {
        binding.book = book
        binding.executePendingBindings()
    }
}