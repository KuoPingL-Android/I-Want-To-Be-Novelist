package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeBooksBinding
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class HomeBookRecyclerAdapter(val viewModel: HomeViewModel): ListAdapter<Book, HomeBookViewHolder>(CallbackFactory().create(Book::class.java)){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBookViewHolder {

        return HomeBookViewHolder(
            ItemHomeBooksBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeBookViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}