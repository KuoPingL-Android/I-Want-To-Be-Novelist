package studio.saladjam.iwanttobenovelist.factories.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.saladjam.iwanttobenovelist.dialog.LoadingDialogViewModel
import java.lang.IllegalArgumentException

class DialogViewModelProviderFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(LoadingDialogViewModel::class.java) -> LoadingDialogViewModel()
                else -> throw IllegalArgumentException("UNKNOWN class ${modelClass}")
            }
        } as T
    }
}
