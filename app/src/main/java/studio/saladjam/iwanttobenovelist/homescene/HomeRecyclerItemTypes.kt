package studio.saladjam.iwanttobenovelist.homescene

enum class HomeRecyclerItemTypes (val id: String, val value: Int) {
    GENERAL("General", 0),
    CURRENT_READING("CurrentReading", 1),
    WORK_IN_PROGRESS("WorkInProgress", 2),
    RECOMMEND("Recommend", 3)
}