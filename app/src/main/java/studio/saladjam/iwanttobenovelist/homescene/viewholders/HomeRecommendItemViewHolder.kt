package studio.saladjam.iwanttobenovelist.homescene.viewholders

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1RecommendBinding
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeRecommendItemViewHolder(val binding: ItemHomeV1RecommendBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: HomeViewModel, section: HomeSections) {
        binding.book = book
        binding.root.isClickable = true
        binding.root.setOnClickListener {
            viewModel.selectBook(book, section)
        }
        binding.executePendingBindings()
    }
}