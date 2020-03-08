package studio.saladjam.iwanttobenovelist.bookdetailscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Categories
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class BookDetailManageViewModel(private val repository: Repository) : ViewModel() {

    private val mWordCountLimit = 100

    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    /** STATUS */
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** DIALOG INFO */
    private val _dialogInfo = MutableLiveData<Pair<String, ApiLoadingStatus>>()
    val dialogInfo: LiveData<Pair<String, ApiLoadingStatus>>
        get() = _dialogInfo

    fun doneDisplayingDialogInfo() {
        _dialogInfo.value = null
    }

    /** TAGET TEXT COUNT */
    val charLimits = MutableLiveData<Int>().apply {
        value = mWordCountLimit
    }

    /** BOOK INFO */
    val book = MutableLiveData<Book>()

    val summary = MutableLiveData<String>()

    fun prepareBook(book: Book) {
        this.book.value = book
        summary.value = book.summary
        fetchCategory()
        fetchChapters()
    }

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>>
        get() = _chapters

    private fun fetchChapters() {

        book.value?.let {book ->

            _status.value = ApiLoadingStatus.LOADING
            _error.value = null
            _dialogInfo.value = Pair("", _status.value!!)

            coroutineScope.launch {
                when(val result = repository.getChaptersIn(book)) {
                    is Result.Success -> {
                        _chapters.value = result.data
                        _status.value = ApiLoadingStatus.DONE
                        _error.value = null
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Fail -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.error
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Error -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                }

            }
        }
    }

    private val _categories = MutableLiveData<Categories>()
    val categories: LiveData<Categories>
        get() = _categories

    private fun fetchCategory() {
        coroutineScope.launch {
            when(val result = repository.getCategory()) {
                is Result.Success -> {
                    _categories.value = result.data
                }

                is Result.Fail -> {

                }

                is Result.Error -> {

                }
            }
        }
    }

    /** BUTTON ACTIONS */
    private val _shouldNavigateBack = MutableLiveData<Boolean>()
    val shouldNavigateBack: LiveData<Boolean>
        get() = _shouldNavigateBack

    fun navigateBack() {
        _shouldNavigateBack.value = true
    }

    fun doneNavigateBack() {
        _shouldNavigateBack.value = null
    }


    var isSaving = false
    fun save() {
        book.value?.summary = summary.value ?: ""

        book.value?.let {book ->

            _status.value = ApiLoadingStatus.LOADING
            _dialogInfo.value = Pair("", _status.value!!)
            _error.value = null
            isSaving = true

            coroutineScope.launch {
                when(val result = repository.updateBook(book)) {
                    is Result.Success -> {
                        _status.value = ApiLoadingStatus.DONE
                        _error.value = null
                        _dialogInfo.value = Pair(IWBNApplication.context.getString(R.string.editor_save), _status.value!!)
                    }

                    is Result.Fail -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.error
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Error -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        _dialogInfo.value = Pair("", _status.value!!)
                    }
                }
            }
        }
    }


}