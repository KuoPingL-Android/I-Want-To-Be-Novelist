package studio.saladjam.iwanttobenovelist.loginscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.repository.Repository

class LoginSelectNameViewModel(private val repository: Repository): ViewModel() {
    val userName = MutableLiveData<String>()

    private val _shouldDismiss = MutableLiveData<Boolean>()
    val shouldDismiss: LiveData<Boolean>
        get() = _shouldDismiss

    init {
        userName.value = IWBNApplication.user.name
    }

    fun updateUserName(name: String) {
        IWBNApplication.user.name = name
        _shouldDismiss.value = true
    }

    fun doneDismiss() {
        _shouldDismiss.value = null
    }
}