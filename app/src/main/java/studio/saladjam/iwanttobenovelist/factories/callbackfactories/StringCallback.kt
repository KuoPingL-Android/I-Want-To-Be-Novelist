package studio.saladjam.iwanttobenovelist.factories.callbackfactories

import androidx.recyclerview.widget.DiffUtil

class StringCallback : DiffUtil.ItemCallback<String>() {
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}