package studio.saladjam.iwanttobenovelist.profilescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileBookitemBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.profilescene.ProfileBookReadingViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ProfileBookReadingAdapter(val viewModel: ProfileBookReadingViewModel): ListAdapter<Book, ProfileBookReadingViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileBookReadingViewHolder {
        return ProfileBookReadingViewHolder(ItemProfileBookitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProfileBookReadingViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            //TODO: SETUP onCLICKLISTENER
        }
    }

}