package studio.saladjam.iwanttobenovelist.constants

import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.extensions.getTime

object TimeConstants {
    val TIME_1000_ms = IWBNApplication.instance.getTime(R.integer.second)
    val TIME_500_ms = IWBNApplication.instance.getTime(R.integer.half_second)
    val TIME_INSTANCE = IWBNApplication.instance.getTime(R.integer.zero_second)
}