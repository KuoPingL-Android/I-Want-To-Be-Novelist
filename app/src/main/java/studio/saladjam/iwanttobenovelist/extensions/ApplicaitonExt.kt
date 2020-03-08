package studio.saladjam.iwanttobenovelist.extensions

import studio.saladjam.iwanttobenovelist.IWBNApplication

fun IWBNApplication.getString(res: Int) : String {
    return this.applicationContext.resources.getString(res)
}

fun IWBNApplication.getFloat(res: Int) : Float {
    return this.applicationContext.resources.getFloat(res)
}

fun IWBNApplication.getPixelSize(res: Int) : Int {
    return this.applicationContext.resources.getDimensionPixelSize(res)
}

fun IWBNApplication.getInt(res: Int) : Int {
    return this.applicationContext.resources.getInteger(res)
}

fun IWBNApplication.getTime(res: Int) : Long {
    return IWBNApplication.instance.getInt(res).toLong()
}

fun IWBNApplication.getStringArray(res: Int) : List<String> {
    return this.applicationContext.resources.getStringArray(res).toList()
}

fun IWBNApplication.getScale(res: Int) : Double {
    val scaleString = getString(res)
    return if (scaleString.contains(":")) {
        val strings = scaleString.split(":")

        if (strings.count() != 2) 0.toDouble()
        else {
            val first = strings.first().toDoubleOrNull() ?: 0.toDouble()
            val second = strings.last().toDoubleOrNull() ?: 0.toDouble()

            val ratio = first / second

            if (ratio.isInfinite()) 0.toDouble() else ratio
        }

    } else {
        scaleString.toDoubleOrNull() ?: 0.toDouble()
    }
}