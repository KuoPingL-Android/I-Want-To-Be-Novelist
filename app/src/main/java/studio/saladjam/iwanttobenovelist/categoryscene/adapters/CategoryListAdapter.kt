package studio.saladjam.iwanttobenovelist.categoryscene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.categoryscene.viewmodel.CategoryListViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBookBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class CategoryListAdapter(val viewModel: CategoryListViewModel): ListAdapter<Book, CategoryListItemViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListItemViewHolder {
        return CategoryListItemViewHolder(ItemCategoryBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryListItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}