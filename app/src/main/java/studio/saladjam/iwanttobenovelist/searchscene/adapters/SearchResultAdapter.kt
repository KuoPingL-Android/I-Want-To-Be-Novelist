package studio.saladjam.iwanttobenovelist.searchscene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemSearchBookBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.searchscene.viewmodel.SearchViewModel

class SearchResultAdapter(private val viewModel: SearchViewModel): ListAdapter<Book, SearchResultViewHolder>(
    CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(ItemSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}