package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1WorkInProgressBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeWorkInProgressViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeWorkInProgressItemAdapter(private val viewModel: HomeViewModel,
                                    private val section: HomeSections,
                                    private val workInProgressViewModel: HomeWorkInProgressViewModel):
    ListAdapter<Book, HomeWorkInProgressItemViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeWorkInProgressItemViewHolder {
        return HomeWorkInProgressItemViewHolder(ItemHomeV1WorkInProgressBinding
            .inflate(LayoutInflater.from(parent.context), parent, false), workInProgressViewModel)
    }

    override fun onBindViewHolder(holder: HomeWorkInProgressItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, section)
    }

    override fun onViewAttachedToWindow(holder: HomeWorkInProgressItemViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    override fun onViewDetachedFromWindow(holder: HomeWorkInProgressItemViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }
}