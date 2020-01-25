package studio.saladjam.iwanttobenovelist.loginscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

class LoginViewModel (private val repository: Repository): ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _shouldNavigateToHomePage = MutableLiveData<Boolean>()
    val shouldNavigateToHomePage: LiveData<Boolean>
        get() = _shouldNavigateToHomePage

    fun navigateToHomePage() {
        _shouldNavigateToHomePage.value = true
    }

    fun navigatedToHomePage() {
        _shouldNavigateToHomePage.value = null
    }

}