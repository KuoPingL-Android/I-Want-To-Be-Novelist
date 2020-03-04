package studio.saladjam.iwanttobenovelist.homescene.viewholders

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeBooksBinding
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeBookViewHolder(val binding: ItemHomeBooksBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: HomeViewModel) {
        binding.book = book
        binding.viewModel = viewModel
        binding.root.setOnClickListener {
            viewModel.selectBook(book)
        }
        binding.executePendingBindings()
    }
}