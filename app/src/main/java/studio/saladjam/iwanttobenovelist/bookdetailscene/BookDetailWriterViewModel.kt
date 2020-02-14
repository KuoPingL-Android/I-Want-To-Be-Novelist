package studio.saladjam.iwanttobenovelist.bookdetailscene

import androidx.lifecycle.*
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

class BookDetailWriterViewModel (private val repository: Repository): ViewModel() {

    var book: Book? = null

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


    //TODO: EXPAND TO ADD NEW SECTION



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


    /** ADD NEW CHAPTER and NAVIGATE to the EDITOR */
    private val _shouldAddChapter = MutableLiveData<Boolean>()
    val shouldAddChapter: LiveData<Boolean>
        get() = _shouldAddChapter

    fun addChapter() {
        if(_shouldAddChapter.value == true) {
            return
        }
        _shouldAddChapter.value = true
    }

    fun donePreparingNewChapter() {
        _shouldAddChapter.value = null
    }

    /** EDIT THE SELECTED CHAPTER */
    private val _selectChapter = MutableLiveData<Chapter>()
    val selectedChapter: LiveData<Chapter>
        get() = _selectChapter

    fun editChapter(chapter: Chapter) {
        _selectChapter.value = chapter
    }

    fun doneNavigatingToChapter() {
        _selectChapter.value = null
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
}