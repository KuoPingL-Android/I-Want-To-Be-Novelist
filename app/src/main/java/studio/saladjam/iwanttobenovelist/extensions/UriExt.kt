package studio.saladjam.iwanttobenovelist.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.zxing.common.BitMatrix
import studio.saladjam.iwanttobenovelist.IWBNApplication
import java.lang.Exception
import java.lang.NullPointerException


fun Uri.getSimpleBitmap(): Bitmap? {
    return try {
        val stream = IWBNApplication.context.contentResolver.openInputStream(this)

        if(stream != null) {
            val bitmap = BitmapFactory.decodeStream(stream)
            bitmap
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

fun Uri.getBitmap(width: Int, height: Int): Bitmap? {

    var rotatedDegree = 0
    var stream = IWBNApplication.context.contentResolver.openInputStream(this)

    /** GET IMAGE ORIENTATION */
    if(stream != null) {
        val exif = ExifInterface(stream)
        rotatedDegree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL).fromExifInterfaceOrientationToDegree()
        stream.close()
    }

    /** GET IMAGE SIZE */
    stream = IWBNApplication.context.contentResolver.openInputStream(this)
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    BitmapFactory.decodeStream(stream, null,options)

    try {
        stream?.close()
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return null
    }

    // The resulting width and height of the bitmap
    if(options.outWidth == -1 || options.outHeight == -1) return null

    var bitmapWidth = options.outWidth.toFloat()
    var bitmapHeight = options.outHeight.toFloat()

    if (rotatedDegree == 90) {
        // Side way -> options.outWidth is actually HEIGHT
        //          -> options.outHeight is actually WIDTH
        bitmapWidth = options.outHeight.toFloat()
        bitmapHeight = options.outWidth.toFloat()
    }

    var scale = 1

    while(true) {
        if(bitmapWidth / 2 < width || bitmapHeight / 2 < height)
            break;
        bitmapWidth /= 2
        bitmapHeight /= 2
        scale *= 2
    }


    val finalOptions = BitmapFactory.Options()
    finalOptions.inSampleSize = scale

    stream = IWBNApplication.context.contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(stream, null, finalOptions)

    try {
        stream?.close()
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return null
    }

    val matrix = Matrix()

    if (rotatedDegree != 0) {
        matrix.preRotate(rotatedDegree.toFloat())
    }

    var bmpWidth = 0

    try {

        if (bitmap == null) {
            return null
        }

        bmpWidth = bitmap.width
    } catch (e: Exception) {
        return null
    }

    var adjustedBitmap = bitmap
    if (bmpWidth > 0) {
        adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    return adjustedBitmap
}