package studio.saladjam.iwanttobenovelist.constants

import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R

object ErrorMessages {
    val NO_NETWORK = IWBNApplication.instance.getString(R.string.internet_not_connected)
    val UNKNOWN = IWBNApplication.instance.getString(R.string.error_unknown)
    val MISS_DATA = IWBNApplication.instance.getString(R.string.error_missing_data)
    val INVALID_TOKEN = IWBNApplication.instance.getString(R.string.error_invalid_token)
}