package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookWriterDetailHeaderBinding
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookDetailWriterHeaderViewHolder (val binding: ItemBookWriterDetailHeaderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, viewModel: BookDetailViewModel) {
        binding.book = book

        //TODO: CONNECT with VIEWMODEL

        binding.executePendingBindings()
    }
}