package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.custom.CharLocator
import studio.saladjam.iwanttobenovelist.custom.Frame
import studio.saladjam.iwanttobenovelist.custom.interceptWith
import studio.saladjam.iwanttobenovelist.extensions.getFontHeight
import studio.saladjam.iwanttobenovelist.extensions.isBasicLatin
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class EditorSimpleContainer @JvmOverloads constructor(context: Context,
                             attrs: AttributeSet? = null,
                             defStyle: Int = 0): FrameLayout(context, attrs, defStyle) {

    init {

    }

    /** TEXT */
    private var mChapter: Chapter? = null
    val chapter: Chapter?
        get() = mChapter

    /** PAINT */
    private var mPaint: Paint? = null
    val paint: Paint?
        get() = mPaint



    fun setContentWithPaint(chapter: Chapter, paint: Paint) {
        mChapter = chapter
        mPaint = paint
    }
    /** TEXT BLOCKS */
    private var mCharPaints = mutableListOf<Paint>()
    private var wordFrames = mutableMapOf<String, Any>()

    fun calculateTextBlocks() {
        val text = chapter?.text ?: ""
        var letterIndex = 0

        text.toCharArray().forEach {
            mCharPaints.add(Paint(mPaint))
        }


        // SEPERATE BY SPACE
        while (letterIndex < text.length) {

            var spaceIndex = text.indexOf(" ", letterIndex) + 1


            if (spaceIndex <= letterIndex) {
                spaceIndex = text.length
            }

            var word = text.substring(letterIndex, spaceIndex)
            var chars = word.toCharArray()
            var indexOfHan = 0

            for(i in 0 until chars.count()) {
                if(!chars[i].isBasicLatin()) {
                    indexOfHan = i
                    break
                }
            }

            if (indexOfHan > 0) {
                word = text.substring(letterIndex, letterIndex + indexOfHan)
            }

            val wordWidth = paint?.measureText(word)



        }
    }

    /** RECORDERs */
    private var pointOfSelection: Point = Point(-1,-1)
    private var lineRecord: MutableMap<Int, List<CharLocator>> = mutableMapOf()
    private var selectedIndex: Int = -2

    private var currentX = 0f
    private var currentY = 0f
    private var charPaints: MutableList<Paint> = mutableListOf()

    private var wordWidth = 0f
    private var spacing = 0f

    private fun getFontHeight(): Float {
        return paint?.getFontHeight() ?: 0f
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        /** DEFAULT */
        currentX = paddingLeft.toFloat()
        currentY = paddingTop.toFloat() + getFontHeight() // (barsHeight - statusHeight) * 1.0f//paddingTop.toFloat() + statusHeight //+ barsHeight

        var counter = 0
        // Seperate Text into Sections
        /** ENGLISH */
        var letterIndex = 0

        val text = chapter?.text ?: ""

        if (charPaints.count() < text.length) {
            for(i in charPaints.count() until text.length) {
                charPaints.add(Paint(mPaint))
            }
        }

        while (letterIndex < text.length) {

            var spaceIndex = text.indexOf(" ", letterIndex) + 1

            if (spaceIndex <= letterIndex) {
                spaceIndex = text.length
            }

            var word = text.substring(letterIndex, spaceIndex)

            if (counter != 0) {
                // Previous wordWidth
                currentX += wordWidth
                Log.i("WORD", "word: ${word}, width=${paint?.measureText(word)}")
            }

            Log.w("CURRENT X, Y", "x=${currentX}, y=${currentY}")
            counter = 1

            // UPDATE WORD WIDTH
            measureText(word)

            if (currentX == paddingLeft.toFloat() && currentX + wordWidth >= width - paddingRight.toFloat()) {
                // A String that is Way too long
                // find the proper size and index

                for(i in 1 until word.length) {
                    val sub = word.substring(0, i)
                    if (checkIfWordFits(sub, width - paddingRight.toFloat())) {
                        word = text.substring(letterIndex, i + 1)
                        paint?.measureText(word)
                        break
                    }
                }
            }

            if (currentX + wordWidth >= width - paddingRight.toFloat()) {
                currentY += paint?.getFontHeight() ?: 0f
                currentX = paddingLeft.toFloat()
            }

            var wordFrame = Frame(currentX.toInt(), currentY.toInt(),
                wordWidth.toInt(), (paint?.getFontHeight() ?: 0f).toInt())

            var viewOfInterest = findWordInterceptingView(wordFrame)
//            val spacing = EditTextBlock.IMAGE_SPACE_DP.toPx(context)

            while (viewOfInterest != null) {
                val frame = imageBlock[viewOfInterest] ?: Frame.zero

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
                    if ((currentY + getFontHeight() >= frame.y &&
                                currentY <= frame.maxY)) {
                        // If it is also horizontally overlapping
                        currentX = frame.maxX.toFloat() + spacing

                        if (currentX + wordWidth > width - paddingRight) {
                            // If the word will reach the end of the EDITTEXT VIEW with PADDING
                            currentX = paddingLeft.toFloat()
                            currentY += getFontHeight()
                        }
                    }
                    // BOTTOM
                    else if ((currentY - getFontHeight() <= frame.maxY&&
                                currentY >= frame.y)) {
                        // If it is also horizontally overlapping
                        currentX = frame.maxX.toFloat() + spacing

                        if (currentX + wordWidth > width - paddingRight) {
                            // If the word will reach the end of the EDITTEXT VIEW with PADDING
                            currentX = paddingLeft.toFloat()
                            currentY += getFontHeight()
                        }
                    }
                }

                wordFrame = Frame(currentX.toInt(), currentY.toInt(), wordWidth.toInt(), getFontHeight().toInt())

                viewOfInterest = findWordInterceptingView(wordFrame)
            }

            if (currentY <= height - spacing - getFontHeight()) {
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
                        getFontHeight().toInt())

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

            } else {
//                text.removeRange(letterIndex - 1, text.length)
//                Log.i("TEXT", "${text}")
                break
            }
            letterIndex += word.length
        }

    }

    private fun checkIfWordFits(word: String, width: Float): Boolean {
        if (mPaint == null) return false

        return mPaint!!.measureText(word) <= width
    }

    private fun measureText(measuringText: String) {
        wordWidth = mPaint?.measureText(measuringText) ?: 0f
    }

    /** IMAGES */
    private val imageBlock = mutableMapOf<EditorImageBlock, Frame>()

    override fun addView(child: View?) {
        super.addView(child)

        if (child is EditorImageBlock) {

            if (imageBlock.contains(child)) return

            val location = IntArray(2)
            child.getLocationInWindow(location)
            val frame = Frame(location[0], location[1], child.width, child.height)
            imageBlock.put(child, frame)
            invalidate()

            child.callback = {
                val location = IntArray(2)
                child.getLocationInWindow(location)
                val frame = Frame(location[0], location[1], child.width, child.height)

                Logger.w("FRAME=${frame}")

                imageBlock[child] =frame
                invalidate()
            }
        }
    }

    private fun findWordInterceptingView(wordFrame: Frame): View? {
        // Take into account the paddings

        imageBlock.keys.forEach {view ->
            val frame = Frame(view.x.toInt(), view.y.toInt(), view.width, view.height)
            imageBlock[view] = frame

            wordFrame.interceptWith(frame)?.let {
                return view
            }
        }

        return null
    }
}