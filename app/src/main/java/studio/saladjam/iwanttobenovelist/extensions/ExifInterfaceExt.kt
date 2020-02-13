package studio.saladjam.iwanttobenovelist.extensions

import android.media.ExifInterface

fun Int.fromExifInterfaceOrientationToDegree(): Int {
    return when(this) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}