package studio.saladjam.iwanttobenovelist.editorscene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_image_block.view.*
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import kotlin.math.absoluteValue

class EditorImageBlock @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0) :
    ConstraintLayout(context, attrs, defStyle) {

    companion object {
        const val MINIMUM_RATIO_TO_PARENT_WIDTH     = 0.3
        const val MINIMUM_RATIO_TO_PARENT_HEIGHT    = 0.1
        const val MAXIMUM_RATIO_TO_PARENT_HEIGHT    = 0.6
        const val DEFAULT_RATIO_TO_PARENT_WIDTH     = 0.3
        const val DEFAULT_RATIO_TO_PARENT_HEIGHT    = 0.1
    }

    private val mainImageView: ImageView
    private val deleteImageView: ImageView
    private val expandImageView: ImageView
    private var deletionEnabled = false

    private var isEditing = false


    fun getImage(): Bitmap {
        val bitmapDrawable = (mainImageView.drawable as BitmapDrawable)
        return bitmapDrawable.bitmap
    }

    fun enableDeletion() {
        deletionEnabled = true
        deleteImageView.visibility = View.VISIBLE
    }

    fun disableDeletion() {
        deletionEnabled = false
        deleteImageView.visibility = View.GONE
    }

    var deletionCallback: ((view: View) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_image_block, this, true).apply {

            mainImageView = image_image_block_image
            deleteImageView = image_image_block_delete
            expandImageView = image_image_block_expand
            deleteImageView.visibility = View.GONE
            expandImageView.visibility = View.GONE


            deleteImageView.setOnClickListener{
                deletionCallback?.let {
                    it(this)
                }
            }


//            mainImageView.isClickable = false
//
//            setOnLongClickListener {
//                if(!isEditing) {
//                    isEditing = !isEditing
//                    closeImageView.visibility = View.VISIBLE
//                    expandImageView.visibility = View.VISIBLE
//                    isLongClickable = false
//                }
//                !isEditing
//            }
//
//            image_image_block_image.setOnClickListener {
//                Logger.i("image_image_block_image")
//                isEditing = true
//                image_image_block_image.isClickable = false
//            }
//
//            isLongClickable = true
//
//            image_image_block_close.setOnClickListener {
//                isEditing = !isEditing
//                closeImageView.visibility = View.GONE
//                expandImageView.visibility = View.GONE
//                isLongClickable = true
//            }
//            image_image_block_expand.setOnTouchListener { v, event ->
//
//                event.offsetLocation(event.rawX - event.x, event.rawY - event.y)
//
//                when (event.actionMasked) {
//
//                    // when being touched
//                    MotionEvent.ACTION_DOWN -> {
//                        // 1 finger
//                        originUp = false
//                        secondOriginUp = false
//                        originX = x - event.getX(0)
//                        originY = y - event.getY(0)
//                    }
//
//                    MotionEvent.ACTION_MOVE -> { // a pointer was moved
//                        var newWidth = event.getX(0) + width
//                        var newHeight = event.getY(0) + height
//
//                        if (newWidth < 50.toPx()) {
//                            newWidth = 50.toPx().toFloat()
//                        }
//                        if (newHeight < 50.toPx()) {
//                            newHeight = 50.toPx().toFloat()
//                        }
//
//                        if (newWidth + view.measuredWidth > (view.parent as View).width) {
//                            newWidth = (view.parent as View).width.toFloat() - view.measuredWidth
//                        }
//                        if (newHeight + view.measuredHeight > (view.parent as View).height) {
//                            newHeight = (view.parent as View).height.toFloat() - view.measuredHeight
//                        }
//
//                        view.x = newWidth
//                        view.y = newHeight
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        originUp = true
//                    }
//                    MotionEvent.ACTION_POINTER_UP -> {
//                        secondOriginUp = true
//                    }
//                }
////
//                false
//            }
        }
    }

//    override fun performClick(): Boolean {
//        return super.performClick()
//    }
//
//    override fun performLongClick(): Boolean {
//        return super.performLongClick()
//    }

    fun setImageBitmap(bitmap: Bitmap?) {
        image_image_block_image.setImageBitmap(bitmap)
    }

    private var wToHRatio = 0f

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        Logger.i("IMAGE RATIO : ${mainImageView.width.toFloat() / mainImageView.height.toFloat()}")
        if(wToHRatio == 0f) {
            wToHRatio = width.toFloat()/height.toFloat()
            Logger.i("OVERALL : ${mainImageView.width.toFloat() / mainImageView.height.toFloat()}")
        }
    }

    private var originX         = 0f
    private var originY         = 0f
    private var secondOriginX   = 0f
    private var secondOriginY   = 0f
    private var lastDiffX       = 0f
    private var lastDiffY       = 0f

    private var originUp = false
    private var secondOriginUp = false

    var touchCallback: ((view: View) -> Unit)? = null
    var bringToFrontCallback: ((view: View) -> Unit)? = null
    var touchUpTouchDownCallback: ((isTouchUp: Boolean) -> Unit)? = null


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Logger.d("isScrollContainer=$isScrollContainer")
        Logger.d("event=$event")



        if (event == null) return false

        // EVENT RAW : Returns the original raw X coordinate of this event on the SCREEN
        // EVENT x/y : ARBITRARY

        /** MAKE SURE EVENT RAWX/RAWY and X/Y are the same */
        event.offsetLocation(event.rawX - event.x, event.rawY - event.y)

        when (event.actionMasked) {
            // when being touched
            MotionEvent.ACTION_DOWN -> {
                parent.bringChildToFront(this)
                bringToFrontCallback?.invoke(this)
                touchUpTouchDownCallback?.invoke(false)
                // 1 finger
                originUp = false
                secondOriginUp = false
                originX = x - event.getX(0)
                originY = y - event.getY(0)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                // Multiple figures
                touchUpTouchDownCallback?.invoke(false)
                secondOriginX = x - event.getX(1)
                secondOriginY = y - event.getY(1)

                lastDiffX = Math.abs(secondOriginX - originX)
                lastDiffY = Math.abs(secondOriginY - originY)

                Logger.e("LASTDIFF=${lastDiffX}, ${lastDiffY}")
            }
            MotionEvent.ACTION_MOVE -> {
                // a pointer was moved
                if (event.pointerCount == 2) {
                    val diffX = (event.getX(1) - event.getX(0)).absoluteValue
                    val diffY = (event.getY(1) - event.getY(0)).absoluteValue
                    var newWidth = (diffX * measuredWidth.toFloat() / lastDiffX).toInt()
                    var newHeight = (diffY * measuredHeight.toFloat() / lastDiffY).toInt()

                    val parentWidth = (parent as View).width
                    val parentHeight = (parent as View).height

                    val params = mainImageView.layoutParams


                    if (newWidth > parentWidth * MINIMUM_RATIO_TO_PARENT_WIDTH &&
                        newHeight > parentHeight * MINIMUM_RATIO_TO_PARENT_HEIGHT) {



                        if (newWidth  > parentWidth) {
                            newWidth = parentWidth - x.toInt()
                            newHeight = (newWidth.toFloat() / wToHRatio).toInt()
                        }

                        if (newHeight > parentHeight * MAXIMUM_RATIO_TO_PARENT_HEIGHT) {
                            newHeight = (parentHeight * MAXIMUM_RATIO_TO_PARENT_HEIGHT).toInt()
                            newWidth = (newHeight / wToHRatio).toInt()
                        }

                        if (wToHRatio > 1) {
                            params.width = newWidth - mainImageView.marginStart - mainImageView.marginEnd
                            params.height = (newWidth / wToHRatio - mainImageView.marginTop - mainImageView.marginBottom).toInt()
                        } else {
                            params.height = newHeight - mainImageView.marginTop - mainImageView.marginBottom
                            params.width = (newHeight * wToHRatio - mainImageView.marginStart - mainImageView.marginEnd).toInt()
                        }

                        mainImageView.layoutParams = params
                        lastDiffX = diffX
                        lastDiffY = diffY
                    } else {
                        if (wToHRatio > 1) {
                            newWidth = (parentWidth * DEFAULT_RATIO_TO_PARENT_WIDTH).toInt()
                            newHeight = (newWidth / wToHRatio).toInt()
                            //FIXME: place the newWidth and newHeight to PARAMS
                        } else {
                            newHeight = (parentHeight * DEFAULT_RATIO_TO_PARENT_HEIGHT).toInt()
                            newWidth = (newHeight * wToHRatio).toInt()
                            //FIXME: place the newWidth and newHeight to PARAMS
                        }
                        mainImageView.layoutParams = params
                        lastDiffX = diffX
                        lastDiffY = diffY
                    }

                } else if (!originUp && !secondOriginUp) {
                    // MOVING 1 FINGER
                    var newX = event.getX(0) + originX
                    var newY = event.getY(0) + originY

                    if (newX < (parent as View).paddingStart) {
                        newX = (parent as View).paddingStart.toFloat()
                    }

                    if (newY < (parent as View).paddingTop) {
                        newY = (parent as View).paddingTop.toFloat()
                    }

                    if (newX + measuredWidth > (parent as View).width - (parent as View).paddingEnd) {
                        newX = (parent as View).width.toFloat() - measuredWidth - (parent as View).paddingEnd
                    }
                    if (newY + measuredHeight > (parent as View).height - (parent as View).paddingBottom) {

                        newY = (parent as View).height.toFloat() - measuredHeight - (parent as View).paddingBottom
                    }

                    x = newX
                    y = newY
                }

                touchCallback?.invoke(this)
            }
            MotionEvent.ACTION_UP -> {
                touchUpTouchDownCallback?.invoke(true)
                originUp = true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                secondOriginUp = true
                touchUpTouchDownCallback?.invoke(true)
            }
        }

        return true
    }

    fun setImageSize(width: Int, height: Int) {
        var imageLayoutParams = mainImageView.layoutParams

        if(imageLayoutParams == null) {
            imageLayoutParams = LayoutParams(width, height)
        }

        mainImageView.layoutParams = imageLayoutParams

    }

    fun setImageUrl(url: String) {
        val storage = IWBNApplication.container.getStorageInstance(url)
        Glide.with(image_image_block_image.context)
            .load(storage)
            //.placeholder(android.R.drawable.gallery_thumb)
            .into(image_image_block_image)
    }


}