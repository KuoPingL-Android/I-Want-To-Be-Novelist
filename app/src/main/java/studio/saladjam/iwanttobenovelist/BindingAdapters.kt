package studio.saladjam.iwanttobenovelist

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailReaderAdapter
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailWriterAdpater
import studio.saladjam.iwanttobenovelist.categoryscene.adapters.CategoryListAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeBookRecyclerAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeRecyclerAdpaterV1
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileBookReadingAdapter
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileWorkAdapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.repository.dataclass.Roles
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchFilterAdapter
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchResultAdapter
import java.util.*
import java.util.concurrent.TimeUnit

/** RECYCLER VIEWs */
@BindingAdapter("categories")
fun bindCategory(recyclerView: RecyclerView, categories: List<Genre>?) {
    (recyclerView.adapter as? LoginInterestRecyclerViewAdapter)?.let {
        it.submitList(categories)
    }
}

@BindingAdapter("homeBooks")
fun bindBook(recyclerView: RecyclerView, books: List<Book>?) {
    val adapter = recyclerView.adapter

    when (adapter) {
        is HomeBookRecyclerAdapter -> adapter.submitList(books)

        is CategoryListAdapter -> adapter.submitList(books)

        is ProfileBookReadingAdapter -> adapter.submitList(books)
    }
}

@BindingAdapter("books")
fun bindBooks(recyclerView: RecyclerView, books: List<Book>?) {
    val adapter = recyclerView.adapter

    when (adapter) {
        is HomeBookRecyclerAdapter -> {
            adapter.submitList(books)
        }

        is CategoryListAdapter -> {
            adapter.submitList(books)
        }

        is ProfileWorkAdapter -> {
            adapter.submitList(books)
        }


    }
}

@BindingAdapter("homeSealedItems")
fun bindHomeSealedItem(recyclerView: RecyclerView, items: List<HomeSealItems>?) {
    val adapter = recyclerView.adapter
    (adapter as? HomeRecyclerAdpaterV1)?.submitList(items)
}

@BindingAdapter("bookDetailSealedItems")
fun bindChapter(recyclerView: RecyclerView, bookDetailSealedItems: List<BookDetailSealedItem>?) {
    val adapter = recyclerView.adapter

    when(adapter) {
        is BookDetailWriterAdpater -> {
            adapter.submitList(bookDetailSealedItems)
        }
        is BookDetailReaderAdapter -> {
            adapter.submitList(bookDetailSealedItems)
        }
    }
}

@BindingAdapter("filters")
fun bindFilter(recyclerView: RecyclerView, filters: List<SearchFilters>?) {
    (recyclerView.adapter as? SearchFilterAdapter)?.submitList(filters)
}

@BindingAdapter("filterResults")
fun bindFilteredResult(recyclerView: RecyclerView, filteredBooks: List<Book>?) {
    (recyclerView.adapter as? SearchResultAdapter)?.submitList(filteredBooks)
}

/** IMAGE URL */
@BindingAdapter("fireStorageImageUrl")
fun bind(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {url ->
        val storage = IWBNApplication.container.getStorageInstance(url)
        Glide.with(imageView.context)
            .load(storage)
            .placeholder(R.drawable.placeholder_icon)
            .into(imageView)
    }
}

/** DATE */
@BindingAdapter("daysSinceLastUpdate")
fun bindDates(textView: TextView, lastUpdatedTime: Long) {
    var finalString = ""
    val currentTime = Calendar.getInstance().timeInMillis
    val diff = currentTime - lastUpdatedTime
    var seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS)
    var minutes = seconds.toFloat() / 60.toFloat()
    var hours = minutes / 24.toFloat()
    var days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    val years = days / 365
    days -= years * 365

    if(years.toInt() > 0) {
        finalString += "${years.toInt()} 年 "
    } else if (days.toInt() > 0) {
        finalString += "${days.toInt()} 天"
    } else if (hours.toInt() > 0) {
        finalString += "${hours.toInt()} 小時"
    } else if (minutes.toInt() > 0) {
        finalString += "${minutes.toInt()} 分鐘"
    } else {
        finalString += "${seconds.toInt()} 秒"
    }

    textView.text = finalString + "前更新"
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

/** PROFILE */
@BindingAdapter("userRole")
fun bindTextView(textView: TextView, role: Int?) {
    role?.let {
        when(it) {
            Roles.REVIEWER.value -> {
                textView.text = IWBNApplication.context.resources.getText(R.string.user_role_reviewer)
            }

            Roles.WRITER.value -> {
                textView.text = IWBNApplication.context.resources.getText(R.string.user_role_writer)
            }
        }
    }
}

@BindingAdapter("booksNumber")
fun bindTextViewToBookCount(textView: TextView, books: Int?) {
    textView.text = if (books == null) "0" else "${books}"
}