package studio.saladjam.iwanttobenovelist.profilescene

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
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class ProfileBookReadingViewModel (private val repository: Repository): ViewModel() {
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** FETCH BOOKs USER is FOLLOWING */
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    private fun fetchBookList() {
        _status.value = APILoadingStatus.LOADING

        if(IWBNApplication.isNetworkConnected) {
            coroutineScope.launch {

                val result = repository.getFollowingBooks(IWBNApplication.user)

                _books.value = when (result) {
                    is Result.Success -> {
                        _status.value = APILoadingStatus.DONE
                        result.data
                    }

                    is Result.Error -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        null
                    }

                    is Result.Fail -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.error
                        null
                    }
                    else -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = "Unknown Error"
                        null
                    }
                }
            }
        } else {
            _status.value = APILoadingStatus.ERROR
            _error.value = "NO INTERNET"
        }
    }
}