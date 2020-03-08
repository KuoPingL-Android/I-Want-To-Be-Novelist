package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.viewmodel.BookDetailViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookWriterDetailHeaderBinding

class BookDetailWriterHeaderViewHolder (val binding: ItemBookWriterDetailHeaderBinding):
    RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun onAttached() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onDetached() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun bind(viewModel: BookDetailViewModel) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}