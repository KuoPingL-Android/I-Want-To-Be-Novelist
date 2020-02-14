package studio.saladjam.iwanttobenovelist.profilescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileWorkitemBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.profilescene.ProfileWorkViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ProfileWorkAdapter(private val viewModel: ProfileWorkViewModel) :ListAdapter<Book, ProfileWorkViewHolder>(
    CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileWorkViewHolder {
        return ProfileWorkViewHolder(ItemProfileWorkitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProfileWorkViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.itemView.setOnClickListener {
            viewModel.editBook(book)
        }
    }

}