package studio.saladjam.iwanttobenovelist.profilescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.constants.ErrorMessages
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private val _user = MutableLiveData<User>().apply {
        value = IWBNApplication.user
    }
    val user: LiveData<User>
        get() = _user

    /** FETCH BOOKs that USER is WORKING ON */
    private val _writtenBooks = MutableLiveData<List<Book>>()
    val writtenBooks: LiveData<List<Book>>
        get() = _writtenBooks

    fun fetchUserWork() {
        _status.value = ApiLoadingStatus.LOADING

        if(IWBNApplication.isNetworkConnected) {
            coroutineScope.launch {

                val result = repository.getUserWork(IWBNApplication.user)

                _writtenBooks.value = when (result) {
                    is Result.Success -> {
                        _status.value = ApiLoadingStatus.DONE
                        result.data
                    }

                    is Result.Error -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        null
                    }

                    is Result.Fail -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.error
                        null
                    }
                    else -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = ErrorMessages.UNKNOWN
                        null
                    }
                }
            }
        } else {
            _status.value = ApiLoadingStatus.ERROR
            _error.value = ErrorMessages.NO_NETWORK
        }
    }
}