package studio.saladjam.iwanttobenovelist.loginscene.adapter

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBinding
import studio.saladjam.iwanttobenovelist.loginscene.LoginInterestViewModel

class CategoryViewHolder (val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(category: String, viewModel: LoginInterestViewModel) {
        binding.viewModel = viewModel
        binding.category = category
        binding.executePendingBindings()
    }
}