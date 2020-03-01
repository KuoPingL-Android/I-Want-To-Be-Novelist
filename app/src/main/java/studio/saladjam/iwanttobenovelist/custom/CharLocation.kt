package studio.saladjam.iwanttobenovelist.custom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import studio.saladjam.iwanttobenovelist.extensions.Frame

@Parcelize
data class CharLocator(val index: Int, val frame: Frame, val char: String) : Parcelable