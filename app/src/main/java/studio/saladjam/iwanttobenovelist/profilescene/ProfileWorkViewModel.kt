package studio.saladjam.iwanttobenovelist.profilescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.constants.ErrorMessages
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class ProfileWorkViewModel(private val repository: Repository): ViewModel() {

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

    private val _shouldCreateNewBook = MutableLiveData<Boolean>()
    val shouldCreateNewBook: LiveData<Boolean>
        get() = _shouldCreateNewBook

    fun createNewBook() {
        _shouldCreateNewBook.value = true
    }

    fun doneShowingCreateBookDialog() {
        _shouldCreateNewBook.value = null
    }


    private val _selectBookToDisplayDetail = MutableLiveData<Book>()
    val selectBookForDisplayDetail: LiveData<Book>
        get() = _selectBookToDisplayDetail

    fun editBook(book: Book) {
        _selectBookToDisplayDetail.value = book
    }

    fun doneShowingBookDetail() {
        _selectBookToDisplayDetail.value = null
    }

    /** NOTIFICATION after BOOK is CREATED */
    private val _finishCreatingBook = MutableLiveData<Boolean>()

    fun notifyBookCreated(book: Book) {
        _finishCreatingBook.value = true
        _selectBookToDisplayDetail.value = book
    }
}