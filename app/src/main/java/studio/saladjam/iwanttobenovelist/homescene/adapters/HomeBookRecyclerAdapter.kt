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

//        val layoutParams =
//            if(viewModel.onlyShowMostPopularBooks.value == true) ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            else ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//
//        val parentLayoutParams =
//            if (viewModel.onlyShowMostPopularBooks.value == true) ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            else parent.layoutParams
//
//        parent.layoutParams = parentLayoutParams
//
//        if(viewModel.onlyShowMostPopularBooks.value == true) {
//            print("HERE")
//        }

        val viewHolder = HomeBookViewHolder(ItemHomeBooksBinding.inflate(LayoutInflater.from(parent.context), parent, false))

//        viewHolder.itemView.rootView.layoutParams = layoutParams

//        val imageLayoutParams =
//            if(viewModel.onlyShowMostPopularBooks.value == true) Con
//        viewHolder.itemView.findViewById<ImageView>(R.id.image_home_item_cover).layoutParams

        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeBookViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}