package studio.saladjam.iwanttobenovelist.customize

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun checkFollowerCount(value: Int): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {

        override fun describeTo(description: Description) {
            description.appendText("Value expected is wrong")
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextView) return false
            val convertedValue = ((item as TextView).text.toString()).toInt()
            return convertedValue == value
        }
    }
}

fun getText(matcher: Matcher<View?>?): String? {
    val stringHolder = arrayOf<String?>(null)

    onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "getting text from a TextView"
        }

        override fun perform(uiController: UiController?, view: View) {
            val tv = view as TextView //Save, because of check in getConstraints()
            stringHolder[0] = tv.text.toString()
        }
    })
    return stringHolder[0]
}