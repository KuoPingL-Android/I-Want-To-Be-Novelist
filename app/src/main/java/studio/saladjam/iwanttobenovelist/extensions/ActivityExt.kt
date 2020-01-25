package studio.saladjam.iwanttobenovelist.extensions

import android.app.Activity
import android.widget.Toast

//fun Activity.getVmFactory(): ViewModelFactory {
////    val repository
//}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}