package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_editor.view.*
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.custom.CharLocator
import studio.saladjam.iwanttobenovelist.extensions.Frame
import studio.saladjam.iwanttobenovelist.extensions.*
import studio.saladjam.iwanttobenovelist.readerscene.ReaderImageBlock
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.ImageBlockRecorder
import java.util.*
import kotlin.math.max


class ScrollableEditorSimpleContainer
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyle: Int = 0): ConstraintLayout(context, attrs, defStyle) {
    /** PARENT SCROLLVIEW */
    var parentScrollView: ScrollView? = null

    /** TEXT */
    private var mChapter: Chapter? = null
    val chapter: Chapter?
        get() = mChapter

    /** PAINT */
    private var mPaint: Paint? = null
    val paint: Paint?
        get() = mPaint

    /** MAXIMUM Y to be Considered */
    private var letterMaxY = 0f

    fun setContentWithPaint(chapter: Chapter, paint: Paint) {
        mChapter = chapter
        mPaint = paint


        mCharPaints.clear()
        words.clear()
        calculateTextBlocks()
    }
    /** TEXT BLOCKS */
    private var mCharPaints = mutableListOf<Paint>()
    private var words = mutableListOf<String>()

    fun calculateTextBlocks() {
        val text = (chapter?.text ?: "").trimEnd()

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
            val chars = word.toCharArray()
            var indexOfInterest = -1

            for(i in 0 until chars.count()) {

                if(chars[i] == '\n' || !chars[i].isBasicLatin()) {

                    val s = chars[i].toString()
                    indexOfInterest = i

                    if (indexOfInterest == 0) {
                        indexOfInterest += 1
                    }

                    break
                }
            }

            if (indexOfInterest > -1) {
                word = text.substring(letterIndex, letterIndex + indexOfInterest)
            }

            words.addAll(divideWordIntoWords(word))
            letterIndex += word.chars().count().toInt()
        }
    }

    private fun divideWordIntoWords(word: String): List<String> {
        measureText(word)
        return if (wordWidth < width - paddingStart - paddingEnd) {
            listOf(word)
        } else {
            val pattern = "\\w+".toRegex()
            val results = pattern.findAll(word, 0)

            if (results.count() == 1) {
                word.toCharArray().map { it.toString() }
            } else {
                val words = mutableListOf<String>()
                var index = 0
                for (i in 0 until results.count()) {
                    val result = results.elementAt(i)
                    val last = result.range.last

                    var subString = word.substring(index)

                    if (i != results.count() - 1) {
                        subString = word.substring(index, last + 1)
                    }

                    measureText(subString)

                    if (wordIsTooLong) {
                        words.addAll(subString.toCharArray().map { it.toString() })
                    } else {
                        words.add(subString)
                    }

                    index = last + 1
                }

                words
            }

        }
    }

    val wordIsTooLong: Boolean
        get() = wordWidth < width - paddingStart - paddingEnd

    /** RECORDERs */
    private var lineRecord: MutableMap<Int, List<CharLocator>> = mutableMapOf()

    private var currentX = 0f
    private var currentY = 0f

    private var wordWidth = 0f
    private var spacing = 8.toPx().toFloat()

    private var currentMaxY = 0f

    private fun getFontHeight(): Float {
        return paint?.getFontHeight() ?: 0f
    }

    private var shouldRedraw = true

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        if (words.isEmpty()) return // NOTHING TO DRAW
        if (!shouldRedraw) return

        /** DEFAULT */
        currentX = paddingLeft.toFloat()
        currentY = paddingTop.toFloat() + getFontHeight()

        var letterIndex = 0

        for (i in 0 until words.size) {

            val word = words[i]

            if (word.toCharArray().first() == '\n')  {
                // if a character == '\n' it will only contain itself
                currentY += getFontHeight()
                currentX = paddingStart.toFloat()
                wordWidth = 0f
                continue
            }

            if (i > 0) {
                currentX += wordWidth
            }

            measureText(word)

            if (currentX + wordWidth >= width - paddingRight.toFloat()) {
                currentY += paint?.getFontHeight() ?: 0f
                currentX = paddingStart.toFloat()
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

                        if (currentY + getFontHeight() >= frame.maxY) {
                            currentY = frame.maxY + spacing
                        }

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

            val wordChars = word.toCharArray()
            var tempCurrentX = currentX

            Logger.e("CHAR=${word}")

            for (charIndex in 0 until word.count()) {
                val charPaint = mCharPaints[letterIndex + charIndex]
                val char = wordChars[charIndex].toString()
                val charWidth = charPaint.measureText(char)

                // REMEMBER all CHARACTER positions
                val charFrame = Frame(
                    tempCurrentX.toInt(),
                    currentY.toInt(),
                    charWidth.toInt(),
                    getFontHeight().toInt())

                if (lineRecord.containsKey(currentY.toInt())) {
                    val list = lineRecord[currentY.toInt()]!!.toMutableList()
                    list.add(CharLocator(letterIndex + charIndex, charFrame, char))
                    lineRecord[currentY.toInt()] =  list

                } else {
                    val list = mutableListOf(CharLocator(letterIndex + charIndex, charFrame, char))
                    lineRecord[currentY.toInt()] = list
                }

                canvas?.drawText(char, tempCurrentX, currentY, charPaint)

                tempCurrentX += charWidth
                letterMaxY = currentY + (paint?.getFontHeight() ?: 0f).toInt() + spacing
            }

            letterIndex += word.length - 1

            currentMaxY = max(letterMaxY,
                (parentScrollView?.height ?: (paddingTop + paddingBottom)
                - paddingTop - paddingBottom).toFloat() )
            if (height != currentMaxY.toInt()) {
                val params = layoutParams
                params.height = currentMaxY.toInt()
                layoutParams = params
//                requestLayout()
            }
        }

        shouldRedraw = false
    }

    private fun measureText(measuringText: String) {
        wordWidth = mPaint?.measureText(measuringText) ?: 0f
    }

    /** IMAGES */
    private val imageBlock = mutableMapOf<EditorImageBlock, Frame>()
    private val bottomToTopImageBlock = mutableListOf<EditorImageBlock>()

    /** ADD BLOCKs when READ from CHAPTERS */
    fun addBlocks(blockRecorders: List<ImageBlockRecorder>, isTouchable: Boolean = false) {
        imageBlock.keys.forEach {
            removeView(it)
        }

        imageBlock.clear()
        bottomToTopImageBlock.clear()

        blockRecorders.forEach {

            val x = it.x.toPx().toFloat()
            val y = it.y.toPx().toFloat()
            val w = it.wToParentWRatio * width
            val h = w / it.wToHRatio

            if (isTouchable) {
                val imageBlock = EditorImageBlock(context)
                var layoutParams = imageBlock.layoutParams

                if (layoutParams == null) {
                    layoutParams = ConstraintLayout.LayoutParams(w.toInt(),h.toInt())
                } else {
                    layoutParams.height = h.toInt()
                    layoutParams.width = w.toInt()
                }

                imageBlock.x = x
                imageBlock.y = y
                imageBlock.layoutParams = layoutParams

                imageBlock.setImageSize(w.toInt(), h.toInt())

                imageBlock.setImageUrl(it.imageLocation)

                addView(imageBlock)
            } else {
                val imageBlock = ReaderImageBlock(context)
                var layoutParams = imageBlock.layoutParams

                if (layoutParams == null) {
                    layoutParams = ConstraintLayout.LayoutParams(w.toInt(),h.toInt())
                } else {
                    layoutParams.height = h.toInt()
                    layoutParams.width = w.toInt()
                }

                imageBlock.x = x
                imageBlock.y = y
                imageBlock.layoutParams = layoutParams

                imageBlock.setImageSize(w.toInt(), h.toInt())

                imageBlock.setImageUrl(it.imageLocation)

                addView(imageBlock)
            }
        }
    }


    override fun addView(child: View?) {
        super.addView(child)

        if (child is EditorImageBlock) {

            if (imageBlock.contains(child)) return

            val frame = Frame(child.x.toInt(), child.y.toInt(), child.width, child.height)

            child.deletionCallback = {
                imageBlock.remove(it)
                bottomToTopImageBlock.remove(it)
                removeView(it)
            }

            if (enableDeletion) {
                child.enableDeletion()
            }

            imageBlock[child] = frame

            bottomToTopImageBlock.add(child)

            bringChildToFront(child)
            shouldRedraw = true
            invalidate()

            child.touchCallback = {
                val frame = Frame(child.x.toInt(), child.y.toInt(), child.width, child.height)
                imageBlock[child] =frame
                shouldRedraw = true
                invalidate()
            }

            child.bringToFrontCallback = {
                if (bottomToTopImageBlock.last() != child)
                    bottomToTopImageBlock.moveToLast(child)
            }

            child.touchUpTouchDownCallback = {isTouchUp ->

            }
        }
    }

    override fun removeView(view: View?) {
        super.removeView(view)
        if (view is EditorImageBlock) {
            imageBlock.remove(view)
            shouldRedraw = false
            invalidate()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
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

    private var enableDeletion = false

    fun enableDeletion() {
        enableDeletion = true
        imageBlock.keys.forEach { it.enableDeletion() }
    }

    fun disableDeletion() {
        enableDeletion = false
        imageBlock.keys.forEach { it.disableDeletion() }
    }

    fun getDetails(): Pair<Map<String, Bitmap>, List<ImageBlockRecorder>> {
        val map = mutableMapOf<String, Bitmap>()
        val blocks = mutableListOf<ImageBlockRecorder>()

        if (imageBlock.keys.isEmpty()) return Pair(map, blocks)

        for (k in bottomToTopImageBlock) {
            val imageID = UUID.randomUUID().toString()
            map[imageID] = k.getImage()
            val v = imageBlock[k]!!
            blocks.add(ImageBlockRecorder(
                imageID,
                x = (v.x.toDp() - paddingStart.toDp()).toInt(),
                y = (v.y.toDp() - paddingTop.toDp()).toInt(),
                wToParentWRatio = v.width.toFloat() / width.toFloat(),
                wToHRatio = v.width.toFloat()/v.height.toFloat(),
                yToParentHRatio = v.y.toFloat()/height.toFloat()))
        }

        return Pair(map, blocks)
    }
}