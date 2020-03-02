package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailManageViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookManageChapterBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class BookDetailManageViewHolder (private val binding: ItemBookManageChapterBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(chapter: Chapter, viewModel: BookDetailManageViewModel) {
        binding.chapter = chapter
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}