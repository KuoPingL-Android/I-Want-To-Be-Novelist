package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class EditorSimpleContainer (context: Context,
                             attrs: AttributeSet? = null,
                             defStyle: Int = 0): View(context, attrs, defStyle) {

    /** TEXT */
    private var mText = ""
    val text: String
        get() = mText

    /** PAINT */
    private var mPaint: Paint? = null
    val paint: Paint?
        get() = mPaint

    fun setContentWithPaint(content: String, paint: Paint) {
        mText = content
        mPaint = paint
    }

    override fun dispatchWindowVisibilityChanged(visibility: Int) {
        super.dispatchWindowVisibilityChanged(visibility)

        Log.i("dispatchWindowVisibilityChanged", "height: ${height} width: ${width}")
    }
}