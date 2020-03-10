package studio.saladjam.iwanttobenovelist.loginscene.adapter

import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBinding
import studio.saladjam.iwanttobenovelist.loginscene.viewmodel.LoginInterestViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre

class CategoryViewHolder (val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(genre: Genre, viewModel: LoginInterestViewModel) {
        binding.viewModel = viewModel
        binding.category = genre.zh
        binding.genre = genre
        binding.executePendingBindings()
    }
}