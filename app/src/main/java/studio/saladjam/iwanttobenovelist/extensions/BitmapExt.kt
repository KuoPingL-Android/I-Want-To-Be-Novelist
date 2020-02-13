package studio.saladjam.iwanttobenovelist.extensions

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur



fun Bitmap.blurRenderScript(context: Context, radius: Float): Bitmap? {

    return try {

        val smallBitmap = rgb565toARGB888()
        val resultBitmap = Bitmap.createBitmap(smallBitmap.width, smallBitmap.height, smallBitmap.config)

        val renderScript = RenderScript.create(context)

        val blurInput = Allocation.createFromBitmap(renderScript, smallBitmap)
        val blurOutput = Allocation.createFromBitmap(renderScript, resultBitmap)
        val blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        blur.setInput(blurInput)
        blur.setRadius(radius)
        blur.forEach(blurOutput)

        blurOutput.copyTo(resultBitmap)

        renderScript.destroy()

        resultBitmap

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Throws(Exception::class)
fun Bitmap.rgb565toARGB888(): Bitmap {
    val numPixels = width * height
    val pixels = intArrayOf(numPixels)

    getPixels(pixels, 0, width, 0, 0, width, height)

    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    result.setPixels(pixels, 0, width, 0, 0, width, height)

    return result
}