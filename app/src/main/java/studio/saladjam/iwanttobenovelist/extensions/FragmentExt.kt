package studio.saladjam.iwanttobenovelist.extensions

import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.factories.viewmodelfactories.ViewModelFactory

fun Fragment.getVMFactory(): ViewModelFactory {
    val repo = IWBNApplication.container.repository
    return ViewModelFactory(repo)
}