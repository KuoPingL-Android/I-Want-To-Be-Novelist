package studio.saladjam.iwanttobenovelist.profilescene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileBookitemBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ProfileBookReadingViewHolder(val binding: ItemProfileBookitemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book) {
        binding.book = book
        binding.executePendingBindings()
    }
}