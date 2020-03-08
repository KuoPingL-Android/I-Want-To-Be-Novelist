package studio.saladjam.iwanttobenovelist.repository.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageBlockRecorder(
    var imageID:            String = "",
    var imageLocation:      String = "",
    var x:                  Int = 0,
    var y:                  Int = 0,
    var wToParentWRatio:    Float = 0f,
    var wToHRatio:          Float = 0f,
    var yToParentHRatio:    Float = 0f
): Parcelable