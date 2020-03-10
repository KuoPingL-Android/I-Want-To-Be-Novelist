package studio.saladjam.iwanttobenovelist.customize

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import studio.saladjam.iwanttobenovelist.IWBNApplication


fun checkIfBitmapMatches(matcher: Matcher<View?>?, resourceId: Int): Boolean {
    var matches = false

    Espresso.onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(ImageView::class.java)
        }

        override fun getDescription(): String {
            return "comparing bitmap of an imageview with resource"
        }

        override fun perform(uiController: UiController?, view: View) {
            (view as ImageView)

            val expectedDrawable =
                IWBNApplication.instance.getDrawable(resourceId)

            expectedDrawable?.let {expected ->
                val bitmap =
                    view.drawable.toBitmap()
                val otherBitmap = expected.toBitmap()
                matches =  bitmap.sameAs(otherBitmap)
            }

        }
    })

    return matches
}

class DrawableTypeMatcher(private val resourceId: Int) : TypeSafeMatcher<View?>() {
    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        if (item !is ImageView) {
            return false
        }
        val imageView: ImageView = item as ImageView
        if (resourceId < 0) {
            return imageView.drawable == null
        }

        val expectedDrawable: Drawable =
            IWBNApplication.instance.getDrawable(resourceId) ?: return false
        val bitmap =
            imageView.drawable.toBitmap()
        val otherBitmap = expectedDrawable.toBitmap()
        return bitmap.sameAs(otherBitmap)
    }

    override fun describeTo(description: Description?) {}
}

object BitmapTestsMatchers {
    fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableTypeMatcher(resourceId) as Matcher<View>
    }

    fun noDrawable(): Matcher<View> {
        return DrawableTypeMatcher(-1) as Matcher<View>
    }


}