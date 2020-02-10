package studio.saladjam.iwanttobenovelist.extensions

import android.util.Log
import android.view.View

data class Frame (var x: Int, var y: Int, var width: Int, var height: Int) {
    val origin: Origin = Origin(x, y)
    companion object {
        val zero = Frame(0,0,0,0)
    }
    val maxX = x + width
    val maxY = y + height
}

data class Origin(val x: Int, val y: Int)

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