package studio.saladjam.iwanttobenovelist.readerscene

import studio.saladjam.iwanttobenovelist.editorscene.EditorImageBlock

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_editor_mixer.view.*
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.custom.CharLocator
import studio.saladjam.iwanttobenovelist.custom.Frame
import studio.saladjam.iwanttobenovelist.custom.interceptWith
import studio.saladjam.iwanttobenovelist.extensions.convertToBitmap
import studio.saladjam.iwanttobenovelist.extensions.getFontHeight
import studio.saladjam.iwanttobenovelist.extensions.isBasicLatin
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.ImageBlockRecorder
import java.io.*
import java.util.*


class ReaderSimpleContainer @JvmOverloads constructor(context: Context,
                                                      attrs: AttributeSet? = null,
                                                      defStyle: Int = 0): FrameLayout(context, attrs, defStyle) {
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
        calculateTextBlocks()
    }
    /** TEXT BLOCKS */
    private var mCharPaints = mutableListOf<Paint>()
    private var words = mutableListOf<String>()

    fun calculateTextBlocks() {
        val text = (chapter?.text ?: "").trimEnd()

        Logger.i("TEXT : $text")

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

            // COLLECT all the LETTERS as an ARRAY
            words.add(word)

            letterIndex += word.chars().count().toInt()
        }
    }

    /** RECORDERs */
    private var lineRecord: MutableMap<Int, List<CharLocator>> = mutableMapOf()

    private var currentX = 0f
    private var currentY = 0f

    private var wordWidth = 0f
    private var spacing = 8.toPx().toFloat()

    private fun getFontHeight(): Float {
        return paint?.getFontHeight() ?: 0f
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        if (words.isEmpty()) return // NOTHING TO DRAW

        /** DEFAULT */
        currentX = paddingLeft.toFloat()
        currentY = paddingTop.toFloat() + getFontHeight() // (barsHeight - statusHeight) * 1.0f//paddingTop.toFloat() + statusHeight //+ barsHeight

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

            if (currentY <= height - spacing - getFontHeight()) {
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
                // STOP DRAWING THE TEXT
                break
            }

            letterIndex += word.length - 1

        }
    }

    private fun measureText(measuringText: String) {
        wordWidth = mPaint?.measureText(measuringText) ?: 0f
    }

    /** IMAGES */
    private val imageBlock = mutableMapOf<ReaderImageBlock, Frame>()

    fun addBlocks(blockRecorders: List<ImageBlockRecorder>) {
        imageBlock.keys.forEach {
            removeView(it)
        }

        imageBlock.clear()

        blockRecorders.forEach {

            val x = it.x.toPx().toFloat()
            val y = it.y.toPx().toFloat()
            val w = it.wToParentWRatio * width
            val h = w / it.wToHRatio

            val imageBlock = ReaderImageBlock(context)
            var layoutParams = imageBlock.layoutParams

            if (layoutParams == null) {
                layoutParams = ConstraintLayout.LayoutParams(w.toInt(),h.toInt())
            } else {
                layoutParams.height = h.toInt()
                layoutParams.width = w.toInt()
            }

//            var imageLayoutParams =

            imageBlock.x = x
            imageBlock.y = y
            imageBlock.layoutParams = layoutParams

            imageBlock.setImageSize(w.toInt(), h.toInt())

            imageBlock.setImageUrl(it.imageLocation)

            addView(imageBlock)
        }
    }

    override fun addView(child: View?) {
        super.addView(child)

        if (child is ReaderImageBlock) {

            if (imageBlock.contains(child)) return
            val frame = Frame(child.x.toInt(), child.y.toInt(), child.width, child.height)

            imageBlock.put(child, frame)

            invalidate()
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