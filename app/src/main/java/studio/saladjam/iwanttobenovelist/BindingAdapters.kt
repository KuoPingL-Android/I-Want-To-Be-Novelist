package studio.saladjam.iwanttobenovelist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter

@BindingAdapter("categories")
fun bind(recyclerView: RecyclerView, categories: List<String>?) {
    (recyclerView.adapter as? LoginInterestRecyclerViewAdapter)?.let {
        it.submitList(categories)
    }
}