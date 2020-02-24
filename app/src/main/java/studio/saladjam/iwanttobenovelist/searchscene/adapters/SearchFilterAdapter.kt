package studio.saladjam.iwanttobenovelist.searchscene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchBookBinding
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchFiltersBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.SearchViewModel

class SearchFilterAdapter(private val viewModel: SearchViewModel): ListAdapter<SearchFilters, SearchFilterViewHolder>(CallbackFactory().create(SearchFilters::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFilterViewHolder {
        return SearchFilterViewHolder(ItemSearchFiltersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchFilterViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

}