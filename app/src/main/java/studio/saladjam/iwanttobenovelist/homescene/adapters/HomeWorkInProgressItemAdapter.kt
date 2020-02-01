package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1WorkInProgressBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeWorkInProgressItemAdapter(val viewModel: HomeViewModel, val section: HomeSections):
    ListAdapter<Book, HomeWorkInProgressItemViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeWorkInProgressItemViewHolder {
        return HomeWorkInProgressItemViewHolder(ItemHomeV1WorkInProgressBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeWorkInProgressItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, section)
    }

}