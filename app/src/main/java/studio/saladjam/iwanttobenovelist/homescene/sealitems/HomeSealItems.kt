package studio.saladjam.iwanttobenovelist.homescene.sealitems

import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

sealed class HomeSealItems {
    data class General(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = "General"
    }
    data class CurrentReading(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = "CurrentReading"
    }
    data class WorkInProgress(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = "WorkInProgress"
    }

    abstract val id: String
}