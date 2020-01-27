package studio.saladjam.iwanttobenovelist.launchscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class LaunchViewModel(private val repository: Repository) : ViewModel() {

    private val _shouldNavigateToHome = MutableLiveData<Boolean>()
    val shouldNavigateToHome: LiveData<Boolean>
        get() = _shouldNavigateToHome

    private val _shouldNavigateToLogin = MutableLiveData<Boolean>()
    val shouldNavigateToLogin: LiveData<Boolean>
        get() = _shouldNavigateToLogin

    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun checkIfFirstTimeUser() {
        val currentUser = IWBNApplication.container.auth.currentUser

        if (currentUser == null) {
            _shouldNavigateToLogin.value = true
        } else {
            _status.value = APILoadingStatus.LOADING
            coroutineScope.launch {
                val result = repository.getUser(currentUser.uid)

                when (result) {
                    is Result.Success -> {
                        IWBNApplication.user = result.data
                        _status.value = APILoadingStatus.DONE
                        _shouldNavigateToHome.value = true
                    }
                    else -> {
                        _status.value = APILoadingStatus.ERROR
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