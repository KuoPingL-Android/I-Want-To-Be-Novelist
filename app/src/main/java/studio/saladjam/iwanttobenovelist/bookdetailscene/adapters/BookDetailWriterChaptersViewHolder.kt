package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.ChapterDetailViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookChapterBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class BookDetailWriterChaptersViewHolder (val binding: ItemBookChapterBinding):
    RecyclerView.ViewHolder(binding.root) , LifecycleOwner {

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

    fun bind(chapter: Chapter, viewModel: ChapterDetailViewModel) {
        binding.chapter = chapter
        binding.viewModel = viewModel

        viewModel.likesForChapters.observe(this, Observer {
            it?.let {
                binding.textBookChapterFavCount.text = "${it[chapter.chapterID] ?: 0}"
            }
        })

        viewModel.fetchLikesForChapter(chapter)

        binding.executePendingBindings()
    }
}