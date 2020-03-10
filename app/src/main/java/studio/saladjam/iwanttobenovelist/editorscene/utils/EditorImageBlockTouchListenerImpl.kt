package studio.saladjam.iwanttobenovelist.editorscene.utils

import android.view.MotionEvent
import android.view.View
import studio.saladjam.iwanttobenovelist.Logger
import kotlin.math.absoluteValue

class EditorImageBlockTouchListenerImpl (val minWidth: Int, val minHeight: Int, val callback: ((view: View)->Unit)? = null) : View.OnTouchListener {

    private var originX = 0f
    private var originY = 0f
    private var secondOriginX = 0f
    private var secondOriginY = 0f
    private var lastDiffX = 0f
    private var lastDiffY = 0f

    private var originUp = false
    private var secondOriginUp = false

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        // EVENT RAW : Returns the original raw X coordinate of this event on the SCREEN
        // EVENT x/y : ARBITRARY

        /** MAKE SURE EVENT RAWX/RAWY and X/Y are the same */
        event.offsetLocation(event.rawX - event.x, event.rawY - event.y)

        when (event.actionMasked) {
            // when being touched
            MotionEvent.ACTION_DOWN -> {
                // 1 finger
                originUp = false
                secondOriginUp = false
                originX = view.x - event.getX(0)
                originY = view.y - event.getY(0)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                // Multiple figures
                secondOriginX = view.x - event.getX(1)
                secondOriginY = view.y - event.getY(1)

                lastDiffX = (secondOriginX - originX).absoluteValue
                lastDiffY = (secondOriginY - originY).absoluteValue
            }
            MotionEvent.ACTION_MOVE -> { // a pointer was moved
                if (event.pointerCount == 2) {
                    // GET TWO FINGERS DISTANCE
                    val diffX = (event.getX(1) - event.getX(0)).absoluteValue
                    val diffY = (event.getY(1) - event.getY(0)).absoluteValue

                    // USE THE PROPORTION between the two DISTANCE to find the SCALE
                    var newWidth = (diffX * view.measuredWidth.toFloat() / lastDiffX).toInt()
                    var newHeight = (diffY * view.measuredHeight.toFloat() / lastDiffY).toInt()

                    // MAKE SURE THE NEW WIDTH and HEIGTH ARE GREATER than the MINWIDTH and HEIGHT
                    // BEFORE SCALING IT via LAYOUTPARAMS
                    if (newWidth > minWidth && newHeight > minHeight) {
                        val parentWidth = (view.parent as View).width
                        val parentHeight = (view.parent as View).height
                        val params = view.layoutParams

                        // MAKE SURE the MAX X is LESS then PARENT WIDTH
                        if (newWidth + view.x > parentWidth) {
                            newWidth = parentWidth - view.x.toInt()
                        }
                        // MAKE SURE the MAX Y is less than PARENT HEIGHT
                        if (newHeight + view.y > parentHeight) {
                            newHeight = parentHeight - view.y.toInt()
                        }

                        params.width = newWidth
                        params.height = newHeight
                        view.layoutParams = params
                        lastDiffX = diffX
                        lastDiffY = diffY
                    }
                } else if (!originUp && !secondOriginUp) {
                    // IF BOTH FINGERS ARE UP, then we can move
                    var newX = event.getX(0) + originX
                    var newY = event.getY(0) + originY

                    if (newX < 0) {
                        newX = 0f
                    }
                    if (newY < 0) {
                        newY = 0f
                    }

                    if (newX + view.measuredWidth > (view.parent as View).width) {
                        newX = (view.parent as View).width.toFloat() - view.measuredWidth
                    }
                    if (newY + view.measuredHeight > (view.parent as View).height) {
                        newY = (view.parent as View).height.toFloat() - view.measuredHeight
                    }

                    view.x = newX
                    view.y = newY
                }

                callback?.invoke(view)
            }
            MotionEvent.ACTION_UP -> {
                originUp = true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                secondOriginUp = true
            }
        }

        return false
    }

}