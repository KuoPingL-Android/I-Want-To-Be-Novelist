package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1GeneralBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

/***/
class HomeGeneralItemAdapter(val viewModel: HomeViewModel, val section: HomeSections) :
    ListAdapter<Book, HomeGeneralItemViewHolder>(CallbackFactory().create(Book::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGeneralItemViewHolder {
        return HomeGeneralItemViewHolder(ItemHomeV1GeneralBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeGeneralItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, section)
    }

}