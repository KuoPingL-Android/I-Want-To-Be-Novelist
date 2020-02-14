package studio.saladjam.iwanttobenovelist.bookdetailscene.adapters

import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

sealed class BookDetailSealedItem {
    data class Header(val book: Book): BookDetailSealedItem() {
        override val id: String
            get() = "HEADER"
    }
    data class Chapters(val chapter: Chapter): BookDetailSealedItem() {
        override val id: String
            get() = "CHAPTER"
    }

    abstract val id : String
}