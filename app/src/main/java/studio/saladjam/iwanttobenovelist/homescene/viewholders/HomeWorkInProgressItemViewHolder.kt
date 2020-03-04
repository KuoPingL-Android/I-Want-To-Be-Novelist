package studio.saladjam.iwanttobenovelist.homescene.viewholders

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1WorkInProgressBinding
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeWorkInProgressViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeWorkInProgressItemViewHolder (val binding: ItemHomeV1WorkInProgressBinding, val workInProgressViewModel: HomeWorkInProgressViewModel)
    : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    /** LIFECYCLE */
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

    /** BINDING */
    fun bind(book: Book, viewModel: HomeViewModel, section: HomeSections) {
        binding.book = book
        binding.root.setOnClickListener {
            viewModel.selectBook(book, section)
        }

        workInProgressViewModel.latestChapters.observe(this, Observer {
            it?.let {chapters ->
                binding.chapter = chapters[book.bookID]
            }
        })

        workInProgressViewModel.numbersOfFollowers.observe(this, Observer {
            it?.let { likes ->
                binding.textItemWorkInProgressHomeV1Likes.text = "${likes[book.bookID]}"
            }
        })

        workInProgressViewModel.addBook(book)

        binding.executePendingBindings()
    }
}