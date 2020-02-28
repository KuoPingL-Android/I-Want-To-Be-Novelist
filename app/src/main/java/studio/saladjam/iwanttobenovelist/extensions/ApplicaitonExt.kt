package studio.saladjam.iwanttobenovelist.extensions

import studio.saladjam.iwanttobenovelist.IWBNApplication

fun IWBNApplication.getString(res: Int): String {
    return this.applicationContext.resources.getString(res)
}