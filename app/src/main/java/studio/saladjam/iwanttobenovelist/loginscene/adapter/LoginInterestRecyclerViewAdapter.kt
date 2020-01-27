package studio.saladjam.iwanttobenovelist.loginscene.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.StringCallback
import studio.saladjam.iwanttobenovelist.loginscene.LoginInterestViewModel

class LoginInterestRecyclerViewAdapter(val viewModel: LoginInterestViewModel)
    : ListAdapter<String, CategoryViewHolder>(StringCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

}