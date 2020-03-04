package studio.saladjam.iwanttobenovelist.homescene.sealitems

import studio.saladjam.iwanttobenovelist.homescene.HomeRecyclerItemTypes
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

sealed class HomeSealItems {
    data class General(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeRecyclerItemTypes.GENERAL.id
    }
    data class CurrentReading(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeRecyclerItemTypes.CURRENT_READING.id
    }
    data class WorkInProgress(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeRecyclerItemTypes.WORK_IN_PROGRESS.id
    }
    data class Recommend(val title: String, val books: List<Book>, val section: HomeSections): HomeSealItems() {
        override val id: String = HomeRecyclerItemTypes.RECOMMEND.id
    }

    abstract val id: String
}