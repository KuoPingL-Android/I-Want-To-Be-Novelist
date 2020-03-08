package studio.saladjam.iwanttobenovelist.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailManageAdapter
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailReaderAdapter
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailWriterAdapter
import studio.saladjam.iwanttobenovelist.categoryscene.adapters.CategoryListAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeBookRecyclerAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeRecyclerAdapterV1
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileBookReadingAdapter
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileWorkAdapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchFilterAdapter
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchResultAdapter

/** RECYCLER VIEWs */
@BindingAdapter("categories")
fun bindCategory(recyclerView: RecyclerView, categories: List<Genre>?) {
    (recyclerView.adapter as? LoginInterestRecyclerViewAdapter)?.let {
        it.submitList(categories)
    }
}

@BindingAdapter("homeBooks")
fun bindBook(recyclerView: RecyclerView, books: List<Book>?) {

    when (val adapter = recyclerView.adapter) {
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
    (adapter as? HomeRecyclerAdapterV1)?.submitList(items)
}

@BindingAdapter("bookDetailSealedItems")
fun bindChapter(recyclerView: RecyclerView, bookDetailSealedItems: List<BookDetailSealedItem>?) {

    when(val adapter = recyclerView.adapter) {
        is BookDetailWriterAdapter -> {
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

@BindingAdapter("chapters")
fun bindChapaters(recyclerView: RecyclerView, chapters: List<Chapter>?) {
    when(val adapter = recyclerView.adapter) {
        is BookDetailManageAdapter -> adapter.submitList(chapters)
    }
}