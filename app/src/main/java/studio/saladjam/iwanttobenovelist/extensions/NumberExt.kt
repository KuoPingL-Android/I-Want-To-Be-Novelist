package studio.saladjam.iwanttobenovelist.extensions

import studio.saladjam.iwanttobenovelist.IWBNApplication

fun Number.toPx(): Int {
    val dp = this.toFloat()
    return (dp * IWBNApplication.context.resources.displayMetrics.density).toInt()
}

fun Number.toDp(): Float {
    val px = this.toFloat()
    return (px / IWBNApplication.context.resources.displayMetrics.density)
}