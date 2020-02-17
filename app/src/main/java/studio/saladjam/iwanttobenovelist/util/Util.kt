package studio.saladjam.iwanttobenovelist.util

import studio.saladjam.iwanttobenovelist.IWBNApplication

object Util {
    fun getString(resourceId: Int): String {
        return IWBNApplication.context.getString(resourceId)
    }
}