package studio.saladjam.iwanttobenovelist.readerscene

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

class ReaderImageBlock @JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0) :
    ConstraintLayout(context, attrs, defStyle) {

    private val mainImageView: ImageView
    private val deleteImageView: ImageView
    private val expandImageView: ImageView

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.layout_image_block, this, true)
            .apply {
                mainImageView = image_image_block_image
                deleteImageView = image_image_block_delete
                expandImageView = image_image_block_expand
                deleteImageView.visibility = View.GONE
                expandImageView.visibility = View.GONE
        }
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
            .into(image_image_block_image)
    }

    var callback: ((view: View) -> Unit)? = null

}