package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters

class SearchFiltersCallback : DiffUtil.ItemCallback<SearchFilters>(){
    override fun areItemsTheSame(oldItem: SearchFilters, newItem: SearchFilters): Boolean {
        return oldItem.string == newItem.string
    }

    override fun areContentsTheSame(oldItem: SearchFilters, newItem: SearchFilters): Boolean {
        return oldItem.string == newItem.string
    }

}