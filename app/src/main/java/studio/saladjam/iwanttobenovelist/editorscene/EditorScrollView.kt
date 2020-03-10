package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout

class EditorScrollView(context: Context,
                       attrs: AttributeSet? = null,
                       defStyle: Int = 0) :
    ScrollView(context, attrs, defStyle) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        return super.onInterceptTouchEvent(ev)
    }

}