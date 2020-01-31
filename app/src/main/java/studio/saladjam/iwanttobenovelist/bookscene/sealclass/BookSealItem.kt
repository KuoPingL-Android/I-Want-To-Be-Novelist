package studio.saladjam.iwanttobenovelist.bookscene.sealclass

import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

sealed class BookSealItem {
    data class BookItem(val chapter: Chapter): BookSealItem()
    object Header:BookSealItem()
}