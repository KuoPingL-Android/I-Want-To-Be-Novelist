package studio.saladjam.iwanttobenovelist.bookscene.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.bookscene.sealclass.BookSealItem
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

//class BookRecyclerAdapter: ListAdapter<BookSealItem, RecyclerView.ViewHolder>(CallbackFactory().create(BookSealItem::class.java)) {
//
//    fun addAndSumitList(chapters: List<Chapter>?) {
//        val chapterItems = chapters?.map { BookSealItem.BookItem(it) }
////        submitList(BookSealItem.Header as BookSealItem +  chapterItems as BookSealItem)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}