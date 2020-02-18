package studio.saladjam.iwanttobenovelist.bookdetailscene

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class BookDetailReaderViewModel (private val repository: Repository): ViewModel() {

    /** COROUTINE */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** NETWORKING STATUS*/
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** BOOK */
//    private val _book = MutableLiveData<Book>()
//    val book: LiveData<Book>
//        get() = _book

    var book: Book? = null

    /** FETCH CHAPTERS */
    /** FETCH ALL CHAPTERS */
    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>>
        get() = _chapters

    fun fetchChapters() {

        _status.value = APILoadingStatus.LOADING

        if (book == null) {
            _error.value = null
            _chapters.value = null
            _status.value = APILoadingStatus.DONE
            return
        }

        coroutineScope.launch {
            val result = repository.getChaptersIn(book!!)

            when(result) {
                is Result.Success -> {
                    _status.value = APILoadingStatus.DONE
                    _error.value = null
                    _chapters.value = result.data
                }

                is Result.Fail -> {
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.error
                    _chapters.value = null
                }

                is Result.Error -> {
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.exception.localizedMessage
                    _chapters.value = null
                }
            }
        }
    }

    /** CONVERT FETCHED CHAPTERS to SEALED ITEMS */
    val sealedItems = MediatorLiveData<List<BookDetailSealedItem>>().apply {
        addSource(_chapters){
            var sealedItem = mutableListOf<BookDetailSealedItem>()

            book?.let {book ->
                sealedItem.add(BookDetailSealedItem.Header(book))
                it?.let {chapters ->
                    chapters.map {chapter ->
                        sealedItem.add(BookDetailSealedItem.Chapters(chapter))
                    }
                }
            }

            value = sealedItem
        }
    }

    /** NAVIGATE to READER PAGE */
    private val _selectedChapter = MutableLiveData<Chapter>()
    val selectedChapter: LiveData<Chapter>
        get() = _selectedChapter

    fun doneNavigateToReader() {
        _selectedChapter.value = null
    }

    /** NAVIGATE back to PREVIOUS PAGE */
    private val _shouldGoBackToPreviousPage = MutableLiveData<Boolean>()
    val shouldGoBackToPreviousPage: LiveData<Boolean>
        get() = _shouldGoBackToPreviousPage

    fun backToPreviousPage() {
        _shouldGoBackToPreviousPage.value = true
    }

    fun doneNavigatingToPreviousPage() {
        _shouldGoBackToPreviousPage.value = null
    }
}