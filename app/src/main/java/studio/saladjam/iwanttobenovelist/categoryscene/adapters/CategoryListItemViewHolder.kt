package studio.saladjam.iwanttobenovelist.categoryscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.categoryscene.viewmodel.CategoryListViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBookBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class CategoryListItemViewHolder (val binding: ItemCategoryBookBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: CategoryListViewModel) {
        binding.book = book
        binding.root.isClickable = true
        binding.root.setOnClickListener {
            viewModel.selectBook(book)
        }
        binding.executePendingBindings()
    }
}