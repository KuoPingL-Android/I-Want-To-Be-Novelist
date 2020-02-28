package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookWriterDetailHeaderBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookDetailWriterHeaderViewHolder (val binding: ItemBookWriterDetailHeaderBinding):
    RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        Log.i("LIFECYCLE", "${lifecycleRegistry.currentState}")
        return lifecycleRegistry
    }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun onAttached() {
        Log.i("FORMVIEWHOLDER", "onATTACHED")
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onDetached() {
        Log.i("FORMVIEWHOLDER", "onCREATED")
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun bind(viewModel: BookDetailViewModel) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}