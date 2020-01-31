package studio.saladjam.iwanttobenovelist

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.saladjam.iwanttobenovelist.extensions.toDp
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeBookRecyclerAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.RecommendRecyclerAdpater
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import java.util.*
import java.util.concurrent.TimeUnit

/** RECYCLER VIEWs */
@BindingAdapter("categories")
fun bindCategory(recyclerView: RecyclerView, categories: List<String>?) {
    (recyclerView.adapter as? LoginInterestRecyclerViewAdapter)?.let {
        it.submitList(categories)
    }
}

@BindingAdapter("homeBooks")
fun bindBook(recyclerView: RecyclerView, books: List<Book>?) {
    val adapter = recyclerView.adapter

    when (adapter) {
        is RecommendRecyclerAdpater -> {
            adapter.submitList(books)
        }

        is HomeBookRecyclerAdapter -> {
            adapter.submitList(books)
        }
    }
}

@BindingAdapter("chapters")
fun bindChapter(recyclerView: RecyclerView, chapters: List<Chapter>?) {
    TODO("IMPLEMENT THIS")
}

/** IMAGE URL */
@BindingAdapter("fireStorageImageUrl")
fun bind(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {url ->
        val storage = IWBNApplication.container.getStorageInstance(url)
        Glide.with(imageView.context)
            .load(storage)
            .into(imageView)
    }
}

/** DATE */
@BindingAdapter("daysSinceLastUpdate")
fun bindDates(textView: TextView, lastUpdatedTime: Long) {
    var finalString = ""
    val currentTime = Calendar.getInstance().timeInMillis
    val diff = currentTime - lastUpdatedTime
    var days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    val years = days / 365
    days -= years * 365

    if(years > 0) {
        finalString += "$years 年 "
    }

    finalString += "$days 天"

    textView.text = finalString
}

/** HOME BOOK ITEM LAYOUTPARAMS */
@BindingAdapter("layoutHomeMainLayout")
fun bindLayoutParams(viewGroup: ViewGroup, isLinearLayout: Boolean) {
    viewGroup.layoutParams =
        if (isLinearLayout) {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
}

@BindingAdapter("layoutHomeInnerLayout")
fun bindInnerLayoutParams(viewGroup: ViewGroup, isLinearLayout: Boolean) {
    viewGroup.layoutParams =
        if (isLinearLayout) {
            viewGroup.layoutParams
        } else {
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
}