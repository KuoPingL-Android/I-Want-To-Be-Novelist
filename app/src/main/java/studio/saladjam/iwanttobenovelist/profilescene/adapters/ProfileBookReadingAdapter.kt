package studio.saladjam.iwanttobenovelist.profilescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileWorkitemBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.profilescene.ProfileBookReadingViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ProfileBookReadingAdapter(val viewModel: ProfileBookReadingViewModel): ListAdapter<Book, ProfileBookReadingViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileBookReadingViewHolder {
        return ProfileBookReadingViewHolder(ItemProfileWorkitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProfileBookReadingViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.itemView.setOnClickListener {
            //TODO: SETUP onCLICKLISTENER
            viewModel.selectBook(book)
        }
    }

}