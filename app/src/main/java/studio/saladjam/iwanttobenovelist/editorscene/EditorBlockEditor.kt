package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class EditorBlockEditor @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0,
            defStyleRes: Int = 0) :
    EditText(context, attrs, defStyle, defStyleRes) {


    companion object {
        const val SPACE_HORIZONTAL_TO_VIEW_ = 20
        const val SPACE_VERTICAL_TO_VIEW = 30
    }

    private var textHeight = 0
    private var textWidth = 0
    private var charHeight = 0f
    private var charWidth = 0f

    private var calculatedWidth = 0
    private var calculatedHeight = 0

    private var currentX = 0f
    private var currentY = 0f

    private var sortedChildren = mutableListOf<View>()

    private var watcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            invalidate()
        }

    }

    init {
        setBackgroundColor(context.resources.getColor(android.R.color.transparent))
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams = params

        // TODO : SET LINE SPACING and OTHER DEFAULT SETTINGs

    }

    fun setTextAndPaint() {

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (sortedChildren.count() >= 0) {
            // Layout current
            for (i in text.toString().indices) {



            }
        }

    }

    fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        child?.let {view ->
            if (sortedChildren.contains(view)) return
            if (!(child is ViewGroup)) {
                sortedChildren.add(child)
                sortedChildren.sortWith(compareBy({it.top}, {it.left}))
                invalidate()
            }
        }
    }

    fun removeView(child: View?) {
        child?.let {view ->
            sortedChildren.remove(view)
            invalidate()
        }
    }

}