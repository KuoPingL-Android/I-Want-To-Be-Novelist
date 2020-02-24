package studio.saladjam.iwanttobenovelist.searchscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchBookBinding
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchFiltersBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.SearchViewModel

class SearchFilterViewHolder(val binding: ItemSearchFiltersBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(filter: SearchFilters, viewModel: SearchViewModel) {
        binding.filter = filter
        binding.root.isClickable = true
        binding.root.setOnClickListener {
            viewModel.select(filter)
        }
        binding.executePendingBindings()
    }

}