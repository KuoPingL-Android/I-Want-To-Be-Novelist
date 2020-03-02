package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.bookdetailscene.BookDetailManageViewModel
import studio.saladjam.iwanttobenovelist.databinding.ItemBookManageChapterBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class BookDetailManageAdapter(private val viewModel: BookDetailManageViewModel) : ListAdapter<Chapter, BookDetailManageViewHolder>(CallbackFactory().create(Chapter::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookDetailManageViewHolder {
        return BookDetailManageViewHolder(ItemBookManageChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookDetailManageViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

}