package studio.saladjam.iwanttobenovelist.homescene

import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R

enum class HomeSections(val id: String, val value: Int, val title: String) {
    GENERAL(
        "General",
        0,
        ""),

    CURRENT_READING(
        "CurrentReading",
        1,
        IWBNApplication.instance.getString(R.string.home_section_currently_reading)),

    WORK_IN_PROGRESS(
        "WorkInProgress",
        2,
        IWBNApplication.instance.getString(R.string.home_section_work_in_progress_title)),

    RECOMMEND(
        "Recommend",
        3,
        IWBNApplication.instance.getString(R.string.home_section_recommend_title)),

    POPULAR(
        "Popular",
        4,
        IWBNApplication.instance.getString(R.string.home_section_popular_title))
}