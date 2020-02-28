package studio.saladjam.iwanttobenovelist.extensions

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.factories.viewmodelfactories.BookViewModelFactory
import studio.saladjam.iwanttobenovelist.factories.viewmodelfactories.DialogViewModelProviderFactory
import studio.saladjam.iwanttobenovelist.factories.viewmodelfactories.ViewModelFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

fun Fragment.getVMFactory(): ViewModelFactory {
    val repo = IWBNApplication.container.repository
    return ViewModelFactory(repo)
}

fun Fragment.getVMFactory(book: Book): BookViewModelFactory {
    val repo = IWBNApplication.container.repository
    return BookViewModelFactory(repo, book)
}

fun Fragment.getDialogFractory(): DialogViewModelProviderFactory {
    return DialogViewModelProviderFactory()
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}