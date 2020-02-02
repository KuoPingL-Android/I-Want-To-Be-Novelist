package studio.saladjam.iwanttobenovelist.loginscene.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.StringCallback
import studio.saladjam.iwanttobenovelist.loginscene.LoginInterestViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre

class LoginInterestRecyclerViewAdapter(val viewModel: LoginInterestViewModel)
    : ListAdapter<Genre, CategoryViewHolder>(CallbackFactory().create(Genre::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

}