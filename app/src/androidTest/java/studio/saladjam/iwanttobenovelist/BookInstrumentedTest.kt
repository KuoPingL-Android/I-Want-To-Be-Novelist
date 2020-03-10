package studio.saladjam.iwanttobenovelist

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import studio.saladjam.iwanttobenovelist.customize.*
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeRecommendViewHolder
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeGeneralViewHolder
import studio.saladjam.iwanttobenovelist.homescene.viewholders.HomeWorkInProgressItemViewHolder


@RunWith(AndroidJUnit4::class)
@LargeTest
class BookInstrumentedTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun findRecyclerView() {
        Thread.sleep(4000)
        onView(withId(R.id.recycler_home_main)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    scrollChildRecyclerView(R.id.recycler_item_home_v1, 1))
        )

        Thread.sleep(1000)
        onView(withId(R.id.recycler_home_main)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    clickItemInChildRecyclerView(R.id.recycler_item_home_v1))
        )

        Thread.sleep(1000)
        val initialCount =
            getText(withId(R.id.text_item_book_writer_fav_count))?.toInt() ?: 0

        Thread.sleep(2000)
        val isFollowing = checkIfBitmapMatches(withId(R.id.image_book_writen_detail_bookmark), R.drawable.bookmarked_icon)

        onView(withId(R.id.image_book_writen_detail_bookmark)).perform(click())

        Thread.sleep(2000)

        if (isFollowing) {
            onView(withId(R.id.text_item_book_writer_fav_count)).check(matches(withText("${initialCount - 1}")))
        } else {
            onView(withId(R.id.text_item_book_writer_fav_count)).check(matches(withText("${initialCount + 1}")))
        }
    }
}