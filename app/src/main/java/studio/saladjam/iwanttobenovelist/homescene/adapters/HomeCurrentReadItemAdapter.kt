package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1CurrentReadingBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeCurrentReadItemViewHolder
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeCurrentReadItemAdapter(val viewModel: HomeViewModel, val section: HomeSections) :
    ListAdapter<Book, HomeCurrentReadItemViewHolder>(
    CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCurrentReadItemViewHolder {
        return HomeCurrentReadItemViewHolder(
            ItemHomeV1CurrentReadingBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeCurrentReadItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, section)
    }

}