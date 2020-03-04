package studio.saladjam.iwanttobenovelist.homescene.sealitems

import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

sealed class HomeSealItems {
    data class General(val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeSections.GENERAL.id
    }
    data class CurrentReading(val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeSections.CURRENT_READING.id
    }
    data class WorkInProgress(val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeSections.WORK_IN_PROGRESS.id
    }
    data class Recommend(val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeSections.RECOMMEND.id
    }

    abstract val id: String
}