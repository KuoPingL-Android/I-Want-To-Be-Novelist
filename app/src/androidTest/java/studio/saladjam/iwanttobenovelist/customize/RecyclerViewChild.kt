package studio.saladjam.iwanttobenovelist.customize

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun scrollChildRecyclerView(id: Int, index: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "scroll child recyclerview with id=$id"
        }

        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(
                    RecyclerView::class.java
                ), ViewMatchers.isDisplayed()
            )
        }

        override fun perform(uiController: UiController?, view: View?) {
            view?.let {
                when(it){
                    is RecyclerView -> {
                        it.smoothScrollToPosition(index)
                    }

                    is ViewGroup -> {
                        it.findViewById<RecyclerView>(id).smoothScrollToPosition(index)
                    }
                }
            }
        }
    }
}

fun clickItemInChildRecyclerView(id: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "perform click on Item at most right position inside recyclerview with id=$id"
        }

        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(
                    RecyclerView::class.java
                ), ViewMatchers.isDisplayed()
            )
        }

        override fun perform(uiController: UiController?, view: View?) {
            view?.let {
                when(it){
                    is RecyclerView -> {
                        it.findChildViewUnder(it.scrollX.toFloat(), it.scrollY.toFloat())
                            ?.performClick()
                    }

                    is ViewGroup -> {
                        it.findViewById<RecyclerView>(id).
                            findChildViewUnder(it.scrollX.toFloat(), it.scrollY.toFloat())
                            ?.performClick()
                    }
                    else -> {

                    }
                }
            }
        }

    }
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Click on a child view with specified id.";
        }

        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(
                    RecyclerView::class.java
                ), ViewMatchers.isDisplayed()
            )
        }

        override fun perform(uiController: UiController?, view: View?) {
            view?.let {
                val v = it.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}