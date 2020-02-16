package studio.saladjam.iwanttobenovelist.extensions

import android.graphics.Paint

fun Paint.getFontHeight(): Float {
    val fm = fontMetrics
    return fm.bottom - fm.top + fm.leading
}