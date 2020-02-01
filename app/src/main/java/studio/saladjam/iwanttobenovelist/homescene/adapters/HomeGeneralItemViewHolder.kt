package studio.saladjam.iwanttobenovelist.homescene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1GeneralBinding
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeGeneralItemViewHolder(val binding: ItemHomeV1GeneralBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: HomeViewModel, section: HomeSections) {
        binding.book = book
        binding.root.isClickable = true
        binding.root.setOnClickListener {
            viewModel.selectBook(book, section)
        }
        binding.executePendingBindings()
    }
}