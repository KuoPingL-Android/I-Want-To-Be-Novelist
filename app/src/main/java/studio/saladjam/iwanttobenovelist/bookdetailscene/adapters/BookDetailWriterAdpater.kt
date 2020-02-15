package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailWriterViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookChapterBinding
import studio.saladjam.iwanttobenovelist.databinding.ItemBookWriterDetailHeaderBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import java.lang.IllegalArgumentException

class BookDetailWriterAdpater(private val viewModel: BookDetailWriterViewModel): ListAdapter<BookDetailSealedItem, RecyclerView.ViewHolder>
    (CallbackFactory().create(BookDetailSealedItem::class.java)) {

    companion object {
        const val BOOK_HEADER = 0
        const val BOOK_CHAPTER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            BOOK_HEADER -> BookDetailWriterHeaderViewHolder(ItemBookWriterDetailHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            BOOK_CHAPTER -> BookDetailWriterChaptersViewHolder(ItemBookChapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("UNKNOWN VIEW TYPE")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is BookDetailWriterHeaderViewHolder -> {
                holder.bind((getItem(position) as BookDetailSealedItem.Header).book, viewModel)
            }

            is BookDetailWriterChaptersViewHolder -> {
                val chapter = (getItem(position) as BookDetailSealedItem.Chapters).chapter
                holder.bind(chapter)
                holder.itemView.setOnClickListener {
                    viewModel.editChapter(chapter)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is BookDetailSealedItem.Header -> BOOK_HEADER
            is BookDetailSealedItem.Chapters -> BOOK_CHAPTER
            else -> throw IllegalArgumentException("ITEM TYPE UNKNOWN")
        }
    }

}