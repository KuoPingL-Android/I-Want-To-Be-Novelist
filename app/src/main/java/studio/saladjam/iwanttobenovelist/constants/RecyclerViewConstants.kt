package studio.saladjam.iwanttobenovelist.constants

import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.extensions.getPixelSize

object RecyclerViewConstants {
    val ITEM_DECORATOR_MARGIN_SMALL
            = IWBNApplication.instance.getPixelSize(R.dimen.item_decorator_small_margin)
    val ITEM_DECORATOR_MARGIN_NORMAL
            = IWBNApplication.instance.getPixelSize(R.dimen.item_decorator_normal_margin)
    val ITEM_DECORATOR_MARGIN_LARGE
            = IWBNApplication.instance.getPixelSize(R.dimen.item_decorator_large_margin)
}