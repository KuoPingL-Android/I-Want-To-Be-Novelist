package studio.saladjam.iwanttobenovelist.homescene.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemRecommendBinding
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class RecommendViewHolder(private val binding: ItemRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: HomeViewModel) {
        binding.book = book
        binding.executePendingBindings()
    }
}