package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.extensions.getPixelSize
import studio.saladjam.iwanttobenovelist.extensions.toPx

class EditorBlock @JvmOverloads
constructor( context: Context,
             attrs: AttributeSet? = null,
             defStyle: Int = 0,
             defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyle, defStyleRes) {

    private val primaryEditText = EditText(context)
    private val primaryPaint: Paint
    private var textHeight = 0



    private var textWidth = 0
    private var charHeight = 0f
    private var charWidth = 0f

    private var calculatedWidth = 0
    private var calculatedHeight = 0

    private var currentX = 0f
    private var currentY = 0f

    private var sortedChildren = mutableListOf<View>()

    private val horizontalMargin = IWBNApplication.instance
        .getPixelSize(R.dimen.editor_block_horizontal_margin)
    private val verticalMargin = IWBNApplication.instance
        .getPixelSize(R.dimen.editor_block_vertical_margin)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_editor_block, this, true)

        // TODO: SET DEFAULT ATTRIBUTES
        primaryPaint = Paint(primaryEditText.paint)

        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        params.
            setMargins(
                horizontalMargin,
                verticalMargin,
                horizontalMargin,
                verticalMargin)

//        primaryEditText.visibility = View.INVISIBLE
        primaryEditText.setTextColor(context.resources.getColor(android.R.color.transparent))

        primaryEditText.layoutParams = params

        primaryEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Logger.i("AFTER")
                invalidate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Logger.i("BEFORE")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.i("ON")
            }

        })

        addView(primaryEditText)
    }

    /**
     * When overriding this method, you must call setMeasuredDimension(int, int)
     * to store the measured width and height of this view.
     *
     * Failure to do so will trigger an IllegalStateException,
     * thrown by measure(int, int). Calling the superclass' onMeasure(int, int) is a valid use.
     * */
    // Measure the view and its content to determine the measured width and the measured height.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        calculatedWidth = measuredWidth
        calculatedHeight = measuredHeight

        measureTextHeight()

        currentX = 0f
        currentY = charHeight

        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)


        val text = primaryEditText.text.toString()

        if (sortedChildren.count() > 0) {

            // Starts from top to bottom
            currentX = 0f
            currentY = charHeight

            //Chinese
            var currentView = sortedChildren.first()

            for (i in text.indices) {
                val subString = text.substring(i, i+1)
                /** SWITCH LINE */
                if (currentX > calculatedWidth - charWidth) {
                    // The Remaining Space is too narrow for even a single character
                    currentY += charHeight
                    currentX = 0f
                }

                /**
                 * CHECK IF TEXT is RIGHT BESIDE the VIEW of INTEREST
                 *
                 * If current CHARACTER is on the LEFT of CURRENT VIEW
                 *  If current CHARACTER is ALIGNED right beside the CURRENT VIEW
                 *
                 *  Check if there is more space for another word or letter
                 *  If NOT -> change currentX to currentView.right
                 *  Else -> keep doing the same thing
                 *
                 * /////   -----------
                 * /////   |         |
                 * /////   |         |
                 * /////   |         |
                 * /////   -----------
                 *
                 * */
                if (currentX <= currentView.left - horizontalMargin &&
                    currentY < currentView.bottom &&
                    currentY >= currentView.top) {

                    if (currentX + charWidth > currentView.left) {
                        currentX = currentView.right.toFloat()
                    }

                }

                /**
                 * CHECK IF TEXT is
                 * */
                if (currentY - charHeight < currentView.bottom &&
                    currentX < currentView.right &&
                    currentX + charWidth > currentView.left) {

                    if (currentX > currentView.top) {
                        currentY = (currentView.bottom + verticalMargin.toPx().toFloat())
                    }

                }

                canvas?.drawText(subString, currentX, currentY, primaryPaint)
                currentX += charWidth
            }
        } else {

            canvas?.drawText(text, currentX, currentY, primaryPaint)
        }
    }

    private fun measureTextHeight() {
        val rect = Rect()
        val text = primaryEditText.text.toString()
        primaryPaint.getTextBounds(text, 0, text.length, rect);
        textWidth = rect.width()
        textHeight = rect.height()

        /** SINGLE WORD or LETTER */
        charWidth = textWidth * 1.0f / text.length
        charHeight = textHeight * 1.0f
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)

        if (child is View && child != primaryEditText && (child is ViewGroup).not()) {
            sortedChildren.add(child)
            sortedChildren.sortWith(compareBy({it.top}, {it.left}))
        }
    }


}