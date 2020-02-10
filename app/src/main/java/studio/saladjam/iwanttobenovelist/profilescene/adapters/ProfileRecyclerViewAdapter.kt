package studio.saladjam.iwanttobenovelist.profilescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileBooklistBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.profilescene.ProfileViewModel
import java.lang.IllegalArgumentException

class ProfileRecyclerViewAdapter(val viewModel: ProfileViewModel) : ListAdapter<String, ProfileBooksViewHolder>(CallbackFactory().create(
    String::class.java)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileBooksViewHolder {
        return ProfileBooksViewHolder(ItemProfileBooklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ProfileBooksViewHolder, position: Int) {
        holder.bind(viewModel, position == 1)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> 0
            1 -> 1
            else -> throw IllegalArgumentException("No such location ${position}")
        }
    }
}