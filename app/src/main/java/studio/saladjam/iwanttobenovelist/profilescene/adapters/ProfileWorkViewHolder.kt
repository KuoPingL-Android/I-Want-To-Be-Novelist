package studio.saladjam.iwanttobenovelist.profilescene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileWorkitemBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ProfileWorkViewHolder(private val binding: ItemProfileWorkitemBinding) :RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book) {
        binding.book = book
        binding.executePendingBindings()
    }
}