package studio.saladjam.iwanttobenovelist.bookdetailscene

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
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class BookDetailManageViewModel(private val repository: Repository) : ViewModel() {
    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    /** STATUS */
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** DIALOG INFO */
    private val _dialogInfo = MutableLiveData<Pair<String, APILoadingStatus>>()
    val dialogInfo: LiveData<Pair<String, APILoadingStatus>>
        get() = _dialogInfo

    /** BOOK INFO */
    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book>
        get() = _book

    fun prepareBook(book: Book) {
        _book.value = book
        fetchChapters()
    }

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>>
        get() = _chapters

    private fun fetchChapters() {

        _book.value?.let {book ->

            _status.value = APILoadingStatus.LOADING
            _error.value = null
            _dialogInfo.value = Pair("", _status.value!!)

            coroutineScope.launch {
                when(val result = repository.getChaptersIn(book)) {
                    is Result.Success -> {
                        _chapters.value = result.data
                        _status.value = APILoadingStatus.DONE
                        _error.value = null
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Fail -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.error
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Error -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                }

            }
        }
    }
}