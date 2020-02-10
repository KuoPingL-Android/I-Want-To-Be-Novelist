package studio.saladjam.iwanttobenovelist.profilescene.adapters

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.ItemProfileBooklistBinding
import studio.saladjam.iwanttobenovelist.profilescene.ProfileViewModel

class ProfileBooksViewHolder (val binding: ItemProfileBooklistBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: ProfileViewModel, isForWorks: Boolean) {

        if (isForWorks) {
            binding.buttonItemProfileCreatebook.visibility = View.VISIBLE
            binding.textItemProfileBooklist.text = "我的作品"
            binding.imageItemProfileBooklist.setImageDrawable(IWBNApplication.context.getDrawable(R.drawable.pen_icon))
        } else {
            binding.buttonItemProfileCreatebook.visibility = View.GONE
            binding.textItemProfileBooklist.text = "追蹤書籍"
            binding.imageItemProfileBooklist.setImageDrawable(IWBNApplication.context.getDrawable(R.drawable.bookmark_icon))
        }
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}