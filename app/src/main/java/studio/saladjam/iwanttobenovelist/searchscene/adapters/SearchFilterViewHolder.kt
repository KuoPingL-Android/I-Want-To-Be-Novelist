package studio.saladjam.iwanttobenovelist.searchscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchFiltersBinding
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.viewmodel.SearchViewModel

class SearchFilterViewHolder(val binding: ItemSearchFiltersBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(filter: SearchFilters, viewModel: SearchViewModel) {
        binding.filter = filter
        binding.root.isClickable = true
        binding.executePendingBindings()
    }
}