package studio.saladjam.iwanttobenovelist.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.marginStart
import studio.saladjam.iwanttobenovelist.extensions.toDp
import studio.saladjam.iwanttobenovelist.extensions.toPx
import android.graphics.*
import android.os.Parcelable
import android.text.Spannable
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.TextView
import kotlinx.android.parcel.Parcelize
import kotlin.math.absoluteValue
import kotlin.math.max


class IWBNEditor @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = android.R.attr.editTextStyle,
            defStyleRes: Int = 0) :
    EditText(context, attrs, defStyle, defStyleRes) {

    companion object {
        const val IMAGE_SPACE_DP= 20
    }

    var startsAddingImages = false

    private var textWidth = 0
    private var textHeight = 0
    private var wordWidth = 0f
    private var fontHeight = 0f
    private var currentX = 0f
    private var currentY = 0f
    private var charPaints: MutableList<Paint> = mutableListOf()

    /** RECORDERs */
    private var pointOfSelection: Point = Point(-1,-1)
    private var lineRecord: MutableMap<Int, List<CharLocator>> = mutableMapOf()
    private var selectedIndex: Int = -2
    private var changedStartIndex: Int = -1
    private var changedLength: Int = -1
    private var currentText: CharSequence? = null


    /** */
    private var shouldShowPointOfInterest = false

    var barsHeight = 0
    //    private var editor = android.widget.Editor
    var editing = false

    private var viewsInfo: MutableMap<View, Frame> = mutableMapOf()

    init {
        setPadding(40.toPx(), 30.toPx(), 40.toPx(), 30.toPx())
    }

    override fun invalidate() {
        super.invalidate()
        // At the beginning, this is null
        lineRecord?.let {
            lineRecord.clear()
        }
    }

//    override fun onPreDraw(): Boolean {
//        return super.onPreDraw()
//    }

//    override fun getExtendedPaddingTop(): Int {
//        return paddingTop
//    }

//    override fun getLineHeight(): Int {
//        return fontHeight.toInt()
//    }

    override fun onDraw(canvas: Canvas?) {
        if (startsAddingImages) {
            /** DEFAULT */
            currentX = paddingLeft.toFloat()
            currentY = paddingTop.toFloat() // (barsHeight - statusHeight) * 1.0f//paddingTop.toFloat() + statusHeight //+ barsHeight

            var counter = 0
            // Seperate Text into Sections
            /** ENGLISH */
            var letterIndex = 0

            if (charPaints.count() < text.length) {
                for(i in charPaints.count() until text.length) {
                    charPaints.add(Paint(paint))
                }
            }

            while (letterIndex < text.length) {

                var spaceIndex = text.indexOf(" ", letterIndex) + 1


                if (spaceIndex <= letterIndex) {
                    spaceIndex = text.length
                }

                var word = text.substring(letterIndex, spaceIndex)

                if (counter != 0) {
                    currentX += wordWidth
                    Log.i("WORD", "width=${wordWidth}")
                }

                measureText(word)

                Log.w("CURRENT X, Y", "x=${currentX}, y=${currentY}")
                counter = 1

                if (currentX == paddingLeft.toFloat() && currentX + wordWidth >= width - paddingRight.toFloat()) {
                    // A String that is Way too long
                    // find the proper size and index

                    for(i in 1 until word.length) {
                        val sub = word.substring(0, i)
                        if (checkIfWordFits(sub, width - paddingRight.toFloat())) {
                            word = text.substring(letterIndex, i + 1)
                            measureText(word)
                            break
                        }
                    }
                }

                if (currentX + wordWidth >= width - paddingRight.toFloat()) {
                    currentY += fontHeight
                    currentX = paddingLeft.toFloat()
                }

                var wordFrame = Frame(currentX.toInt(), currentY.toInt(),
                    wordWidth.toInt(), fontHeight.toInt())

                var viewOfInterest = findWordInterceptingView(wordFrame)
                val spacing = IMAGE_SPACE_DP.toPx()

                while (viewOfInterest != null) {
                    val frame = viewsInfo[viewOfInterest] ?: Frame.zero

                    /**
                     *         //////////
                     *        / ________
                     *        /|--------|
                     *        /|--------|
                     *        /|--------|
                     *        /|--------|
                     *        / --------
                     *         //////////
                     *
                     */

                    if ((currentX + wordWidth >= frame.x && currentX  <= frame.maxX) || (currentX <= frame.maxX && currentX > frame.x) ) {
                        // If it is vertically overlapping
                        // TOP
                        if ((currentY + fontHeight >= frame.y - barsHeight &&
                                    currentY <= frame.maxY - barsHeight)) {
                            // If it is also horizontally overlapping
                            currentX = frame.maxX.toFloat() + spacing

                            if (currentX + wordWidth > width - paddingRight) {
                                // If the word will reach the end of the EDITTEXT VIEW with PADDING
                                currentX = paddingLeft.toFloat()
                                currentY += fontHeight
                            }
                        }
                        // BOTTOM
                        else if ((currentY - fontHeight <= frame.maxY  - barsHeight &&
                                    currentY >= frame.y - barsHeight)) {
                            // If it is also horizontally overlapping
                            currentX = frame.maxX.toFloat() + spacing

                            if (currentX + wordWidth > width - paddingRight) {
                                // If the word will reach the end of the EDITTEXT VIEW with PADDING
                                currentX = paddingLeft.toFloat()
                                currentY += fontHeight
                            }
                        }
                    }

                    wordFrame = Frame(currentX.toInt(), currentY.toInt(), wordWidth.toInt(), fontHeight.toInt())

                    viewOfInterest = findWordInterceptingView(wordFrame)
                }

//                if (currentY <= height - spacing - fontHeight) {
                    val wordChars = word.toCharArray()
                    var tempCurrentX = currentX
                    for (charIndex in 0 until word.count()) {
                        val charPaint = charPaints[letterIndex + charIndex]
                        val char = wordChars[charIndex].toString()
                        val charWidth = charPaint.measureText(char)

                        // REMEMBER all CHARACTER positions
                        val charFrame = Frame(
                            tempCurrentX.toInt(),
                            currentY.toInt(),
                            charWidth.toInt(),
                            fontHeight.toInt())

                        if (lineRecord.containsKey(currentY.toInt())) {
                            val list = lineRecord.get(currentY.toInt())!!.toMutableList()
                            list.add(CharLocator(letterIndex + charIndex, charFrame, char))
                            lineRecord.put(currentY.toInt(), list)

                        } else {
                            val list = mutableListOf(CharLocator(letterIndex + charIndex, charFrame, char))
                            lineRecord.put(currentY.toInt(), list)
                        }

                        canvas?.drawText(char, tempCurrentX, currentY, charPaint)

                        tempCurrentX += charWidth
                    }

//                } else {
////                text.removeRange(letterIndex - 1, text.length)
////                Log.i("TEXT", "${text}")
//                    break
//                }
                letterIndex += word.length
            }

            findLetterPositionFor(pointOfSelection)

            if(shouldShowPointOfInterest) {
                if (charPaints.count() == 0) {
                    canvas?.drawLine(currentX, fontHeight, currentX, currentY - fontHeight, paint)
                } else {
                    val charPaint = charPaints[selectedIndex]
                    val fm = charPaint.fontMetrics
                    val fmHeight = fm.bottom - fm.top + fm.leading
                    canvas?.drawLine(pointOfSelection.x.toFloat(),
                        (pointOfSelection.y + fm.top - fm.leading),
                        pointOfSelection.x.toFloat(),
                        (pointOfSelection.y +fm.bottom).toFloat(), charPaints.first())
                }
            }
        } else {
            Log.i("DISPLAY SUPER", "SUPER")
            super.onDraw(canvas)
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, selectedIndex, lengthBefore, lengthAfter)
    }

    private fun findWordInterceptingView(wordFrame: Frame): View? {
        // Take into account the paddings

        viewsInfo.keys.forEach {view ->
            val frame = (viewsInfo[view] ?: Frame.zero).copy()

            wordFrame.interceptWith(frame)?.let {
                return view
            }
        }

        return null
    }

//    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback?) {
//        super.setCustomSelectionActionModeCallback(actionModeCallback)
//    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureOverallText()
    }

    private fun measureText(measuringText: String) {
        wordWidth = paint.measureText(measuringText)
    }

    private fun checkIfWordFits(word: String, width: Float): Boolean {
        return paint.measureText(word) <= width
    }

    private fun measureOverallText() {
        val rect = Rect()
        val measuringText = text.toString()
        paint.getTextBounds(measuringText, 0, measuringText.length, rect)
        textWidth = rect.width()
        textHeight = rect.height()
        val fm = paint.fontMetrics
        fontHeight = fm.bottom - fm.top + fm.leading
        lineHeight = fontHeight.toInt()

        print("Test")

    }

    fun updateOrAdd(view: View, frame: Frame) {
        shouldShowPointOfInterest = false

        val space = IMAGE_SPACE_DP.toPx()

        if (frame.x >= space) frame.x -= space
        if (frame.y >= space) frame.y <= space
        frame.width += space
        frame.height += space

        if (!viewsInfo.containsKey(view)) {
            addView(view, frame)
        } else {
            viewsInfo[view] = frame
        }
        invalidate()
    }

    private fun addView(view: View, frame: Frame) {
        viewsInfo.put(view, frame)
    }

    fun removeView(view: View) {
        viewsInfo.remove(view)
    }

//    private fun calculateCursorLocationAt(point: Point) {
//
//        findLetterPositionFor(point)?.let {charLocator ->
//
//            val bestFitY = charLocator.frame.y
//
//            selectedIndex = charLocator.index
//
//            Log.i("SELECTED CHAR INDEX", "selectedIndex=$selectedIndex")
//
//            var bestFitX = charLocator.frame.x ?: 0
//            if ((point.x - bestFitX).absoluteValue >
//                (point.x - charLocator.frame.maxX).absoluteValue) {
//                bestFitX = charLocator.frame.maxX
//            }
//
//            if (shouldShowPointOfInterest) {
//                if (pointOfSelection.y != bestFitY || pointOfSelection.x != bestFitX) {
//                    pointOfSelection.x = bestFitX
//                    pointOfSelection.y = bestFitY
//                    shouldShowPointOfInterest = true
//                }
//            }
//            Log.i("SELECTING INDEX", "index=${charLocator.index}")
////            setSelection(selectedIndex)
//        }
//    }

//    private fun setCursorLocationAtSelectedIndex() {
//        if(shouldShowPointOfInterest) {
//            var isLarger = false
//            var isSmaller = false
//            val yPositions = lineRecord.keys.sorted()
//
//            lineRecord[pointOfSelection.y]?.forEach {
//                if (it.index == selectedIndex) {
//                    pointOfSelection.x = it.frame.x
//                    isLarger = false
//                    return@forEach
//                }
//                isLarger = it.index > selectedIndex
//                isSmaller = it.index < selectedIndex
//            }
//
//            if (isLarger) {
//
//            } else if (isSmaller) {
//
//            }
//        }
//    }

    override fun setSelection(index: Int) {
        super.setSelection(index)

    }

//    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
//        if(shouldShowPointOfInterest) {
//            Log.i("SELECT POINT ", "start=$selStart, end=$selEnd")
//            setSelection(selectedIndex)
//        } else {
//            Log.i("SELECT DEFAULT", "start=$selStart, end=$selEnd")
//            super.onSelectionChanged(selStart, selEnd)
//        }
//    }

    private fun findLetterPositionFor(point:Point): CharLocator? {

        if(point.x == -1 || point.y == -1) return null

        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        lineRecord.keys.sorted().forEach {
            if (point.y >= it) {
                minY = it
            } else {
                if (maxY < 0) {
                    maxY = it
                }
            }
        }

        var bestFitY = minY

        if ((minY - point.y).absoluteValue > (maxY - point.y).absoluteValue) {
            // max is the better choice
            bestFitY = maxY
        }

        var charLocators = lineRecord[bestFitY]

        var charLocator: CharLocator? = null

        charLocators?.forEach {locator ->
            if (point.x >= locator.frame.x) {
                charLocator = locator
            }
        }

        charLocator?.let {
            Log.w("CHAR", "char=${charLocator?.char}")
        }

        return charLocator
    }

//    override fun onTextChanged(
//        text: CharSequence?,
//        start: Int,
//        lengthBefore: Int,
//        lengthAfter: Int
//    ) {
//        super.onTextChanged(text, start, lengthBefore, lengthAfter)
//
//        if (shouldShowPointOfInterest) {
//            if (selectedIndex > 0) {
//                selectedIndex+=(lengthAfter - lengthBefore)
//            }
//            setSelection(selectedIndex)
//        }
//    }
}

@Parcelize
data class Frame (var x: Int, var y: Int, var width: Int, var height: Int): Parcelable {
    val origin: Origin
        get() = Origin(x, y)
    companion object {
        val zero = Frame(0,0,0,0)
    }
    val maxX:Int
        get() = x + width
    val maxY: Int
        get() = y + height
}

fun Frame.interceptWith(frame: Frame): Frame? {

    // Check if one of them contains the other one
    val sortedX = listOf(x, maxX, frame.x, frame.maxX).sorted()

    val sortedY = listOf(y, maxY, frame.y, frame.maxY).sorted()

    var overlappingFrame: Frame? = Frame(sortedX[1], sortedY[1],
        sortedX[2] - sortedX[1], sortedY[2] - sortedY[1])

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

data class Origin(val x: Int, val y: Int)

fun View.frameInSurface(): Frame {

    val array = IntArray(2)

    getLocationInWindow(array)

    return Frame(array.first(), array.last(), width, height)
}

fun View.frameInWindow(): Frame {
    val array = IntArray(2)

    getLocationInWindow(array)

    return Frame(array.first(), array.last(), width, height)
}

fun View.positionIn(view: View): Frame {
    val mFrame = this.frameInWindow()
    val nFrame = view.frameInWindow()

    val x = mFrame.x - nFrame.x
    val y = mFrame.y - nFrame.y

    val width = if (x < 0) mFrame.width + x else mFrame.width
    val height = if (y < 0) mFrame.height + y else mFrame.height


    return Frame(max(x, 0), max(y, 0), max(width, 0), max(height, 0) )
}

fun View.findOverlap(withView: View): Frame {

    var frameA = frameInSurface()
    var frameB = withView.frameInSurface()

    return frameA.interceptWith(frameB)?: Frame.zero
}

@Parcelize
data class CharLocator(val index: Int, val frame: Frame, val char: String) : Parcelable