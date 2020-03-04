package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.layout_editor_mixer_v1.view.*
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.extensions.toPx

/** this is the LAYOUT that holds VIEWPAGER within and DETERMINE the number of FRAGMENT needs to be displayed */
class EditorMixerV1 @JvmOverloads constructor(context: Context?,
                    attrs: AttributeSet? = null,
                    defStyle: Int = R.style.EditorMixer): ConstraintLayout(context, attrs, defStyle) {

    private var _mViewpager: ViewPager2
    private var _mViewPagerWidth = 0
    private var _mViewPagerHeight = 0
    private var _shouldRecalculateFitting = false


    init {

        LayoutInflater.from(context).inflate(R.layout.layout_editor_mixer_v1, this, true).apply {
            _mViewpager = viewpager_mixer
            _mPaint = edit_mixer.paint
        }

        invalidate()
        startFitting()
    }

    private var _mPaint:Paint? = null
    val paint: Paint?
        get() = _mPaint

    fun setPaint(paint: Paint) {
        _mPaint = paint
        startFitting()
    }

    private var _mText: String = ""
    val text: String
        get() = _mText

    fun setText(text: String) {
        _mText = text
        startFitting()
    }

    fun setTextWithPaint(text: String, paint: Paint) {
        _mPaint = paint
        _mText = text
        startFitting()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (_mViewPagerWidth != _mViewpager.width || _mViewPagerHeight != _mViewpager.height) {
            _mViewPagerWidth = _mViewpager.width
            _mViewPagerHeight = _mViewpager.height
            _shouldRecalculateFitting = true
            invalidate()
        }

        startFitting()

        Logger.i("dispatchDraw : ${width} ${height}")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Logger.i("onDraw : ${width} ${height}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Logger.i("onAttachedToWindow : ${width} ${height}")
    }

    private fun startFitting() {
        _shouldRecalculateFitting = false



    }
}