package studio.saladjam.iwanttobenovelist.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Parcelable
import android.util.Log
import android.view.TouchDelegate
import android.view.View
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Frame (var x: Int, var y: Int, var width: Int, var height: Int, var z: Int = 0): Parcelable {
    @IgnoredOnParcel
    val origin: Origin = Origin(x, y)
    companion object {
        val zero = Frame(0,0,0, 0,0)
    }
    @IgnoredOnParcel
    val maxX = x + width
    @IgnoredOnParcel
    val maxY = y + height
}

@Parcelize
data class Origin(val x: Int, val y: Int): Parcelable

fun View.frameInSurface(): Frame {

    val array = IntArray(2)

    getLocationOnScreen(array)

    return Frame(array.first(), array.last(), width, height)
}

fun View.findOverlap(withView: View): Frame {

    var frameA = frameInSurface()
    var frameB = withView.frameInSurface()

    // Check if one of them contains the other one
    val sortedX = listOf(frameA.x, frameA.maxX, frameB.x, frameB.maxX).sorted()

    val sortedY = listOf(frameA.y, frameA.maxY, frameB.y, frameB.maxY).sorted()


    var overlappingFrame = Frame(sortedX[1], sortedY[1],
        sortedX[2] - sortedX[1], sortedY[2] - sortedY[1])


    if ((overlappingFrame.x == frameA.maxX && overlappingFrame.maxX == frameB.x) ||
        (overlappingFrame.x == frameB.maxX && overlappingFrame.maxX == frameA.x)) {
        overlappingFrame = Frame.zero
    } else if ((overlappingFrame.y == frameA.maxY && overlappingFrame.maxY == frameB.y) ||
        (overlappingFrame.y == frameB.maxY && overlappingFrame.maxY == frameA.y)) {
        overlappingFrame = Frame.zero
    }


    return overlappingFrame
}

fun View.convertToBitmap(): Bitmap {
    val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(measureSpec, measureSpec)
    layout(0, 0, measuredWidth, measuredHeight)
    val b = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    b.eraseColor(Color.TRANSPARENT)
    val c = Canvas(b)

    val bgDrawable = background

    if (bgDrawable != null) {
        bgDrawable.draw(c)
    } else {
        c.drawColor(Color.WHITE)
    }


    draw(c)
    return b
}

/**
 * Increase touch area of the view/button .
 */
fun View.setTouchDelegate() {
    val parent = this.parent as View  // button: the view you want to enlarge hit area
    parent.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= 100    // increase top hit area
        rect.left -= 100   // increase left hit area
        rect.bottom += 100 // increase bottom hit area
        rect.right += 100  // increase right hit area
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}

fun Frame.interceptWith(frame: Frame): Frame? {

    // Check if one of them contains the other one
    val sortedX = listOf(x, maxX, frame.x, frame.maxX).sorted()

    val sortedY = listOf(y, maxY, frame.y, frame.maxY).sorted()

    var overlappingFrame: Frame? =
        Frame(
            sortedX[1], sortedY[1], sortedX[2] - sortedX[1], sortedY[2] - sortedY[1]
        )

    if ((overlappingFrame!!.x == maxX && overlappingFrame.maxX == frame.x) ||
        (overlappingFrame.x == frame.maxX && overlappingFrame.maxX == x)) {
        return null
    }
    if ((overlappingFrame.y == maxY && overlappingFrame.maxY == frame.y) ||
        (overlappingFrame.y == frame.maxY && overlappingFrame.maxY == y)) {
        return null
    }
    return overlappingFrame
}