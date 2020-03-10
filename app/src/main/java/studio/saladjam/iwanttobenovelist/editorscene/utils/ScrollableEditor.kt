//package studio.saladjam.iwanttobenovelist.editorscene.utils
//
//import android.content.Context
//import android.view.MotionEvent
//import android.view.ViewConfiguration
//import android.view.ViewGroup
//import android.widget.Scroller
//
//class ScrollableEditor @JvmOverloads constructor(
//    context: Context,
//    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
//) : ViewGroup(context) {
//
//    private var mIsScrolling = false
//    private var mIsChildMoving = false
//    private var mScroller = Scroller(context)
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        /*
//         * This method JUST determines whether we want to intercept the motion.
//         * If we return true, onTouchEvent will be called and we do the actual
//         * scrolling there.
//         */
//        return when (ev.actionMasked) {
//            // Always handle the case of the touch gesture being complete.
//            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//                // Release the scroll.
//                mIsScrolling = false
//                false // Do not intercept touch event, let the child handle it
//            }
//            MotionEvent.ACTION_MOVE -> {
//
//                if (mIsScrolling) true
//                else !mIsChildMoving
//            }
//
//            else -> {
//                // In general, we don't want to intercept touch events. They should be
//                // handled by the child view.
//                false
//            }
//        }
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
//        // scroll this container).
//        // This method will only be called if the touch event was intercepted in
//        // onInterceptTouchEvent
//    }
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}