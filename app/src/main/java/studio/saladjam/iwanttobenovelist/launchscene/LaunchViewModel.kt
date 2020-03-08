package studio.saladjam.iwanttobenovelist.launchscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.UserManager
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class LaunchViewModel(private val repository: Repository) : ViewModel() {

    private val _shouldNavigateToHome = MutableLiveData<Boolean>()
    val shouldNavigateToHome: LiveData<Boolean>
        get() = _shouldNavigateToHome

    private val _shouldNavigateToLogin = MutableLiveData<Boolean>()
    val shouldNavigateToLogin: LiveData<Boolean>
        get() = _shouldNavigateToLogin

    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun checkIfFirstTimeUser() {
        val userID = UserManager.userID

        if (userID == null) {
            _shouldNavigateToLogin.value = true
        } else {
            _status.value = ApiLoadingStatus.LOADING
            coroutineScope.launch {
                when (val result = repository.getUser(userID)) {
                    is Result.Success -> {
                        IWBNApplication.user = result.data
                        _status.value = ApiLoadingStatus.DONE
                        _shouldNavigateToHome.value = true
                    }
                    else -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _shouldNavigateToLogin.value = true
                    }
                }
            }
        }
    }

    fun doneNavigatingToHome() {
        _shouldNavigateToHome.value = null
    }

    fun doneNavigatingToLogin() {
        _shouldNavigateToLogin.value = null
    }
}