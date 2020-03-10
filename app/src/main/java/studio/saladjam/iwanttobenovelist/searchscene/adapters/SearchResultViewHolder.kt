package studio.saladjam.iwanttobenovelist.searchscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchBookBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.searchscene.viewmodel.SearchViewModel

class SearchResultViewHolder(val binding: ItemSearchBookBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book, viewModel: SearchViewModel) {
        binding.book = book
        binding.root.isClickable = true
        binding.root.setOnClickListener {
            viewModel.selectBook(book)
        }
        binding.executePendingBindings()
    }

}