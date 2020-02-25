package studio.saladjam.iwanttobenovelist.homescene

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class HomeWorkInProgressViewModel (private val repository: Repository): ViewModel() {

    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** DATAs */
    private val _latestChapters = MutableLiveData<MutableMap<String, Chapter?>>()
        .apply {
        value = mutableMapOf()
        }
    val latestChapters: LiveData<MutableMap<String, Chapter?>>
        get() = _latestChapters

    private val _numbersOfFollowers = MutableLiveData<MutableMap<String, Int>>()
        .apply {
            value = mutableMapOf()
        }
    val numbersOfFollowers: LiveData<MutableMap<String, Int>>
        get() = _numbersOfFollowers


    private val _chapterStatues = MutableLiveData<MutableMap<String, APILoadingStatus>>()
        .apply {
            value = mutableMapOf()
        }
    val chapterStatues: LiveData<MutableMap<String, APILoadingStatus>>
        get() = _chapterStatues

    private val _followingStatuses = MutableLiveData<MutableMap<String, APILoadingStatus>>()
        .apply {
            value = mutableMapOf()
        }
    val followingStatuses: LiveData<MutableMap<String, APILoadingStatus>>
        get() = _followingStatuses

    val statuses = MediatorLiveData<MutableMap<String, APILoadingStatus>>().apply {
        addSource(_chapterStatues) {  }
        addSource(_followingStatuses) { }
    }

    private val _errors = MutableLiveData<MutableMap<String, String>>()
        .apply {
            value = mutableMapOf()
        }
    val errors: LiveData<MutableMap<String, String>>
        get() = _errors


    private val _books = mutableMapOf<String, Book>()
    private val _bookIDs = mutableListOf<String>()

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    fun selected(book: Book) {
        _selectedBook.value = book
    }

    fun doneSelectingBook() {
        _selectedBook.value = null
    }

    fun addBook(book: Book) {
        if (_books.filter { it.key == book.bookID }.isNotEmpty()) {
            return
        }

        _books[book.bookID] = book

        fetchLatestChapterFor(book)
        fetchNumberOfFollowersFor(book)
    }

    private fun fetchLatestChapterFor(book: Book) {
        _chapterStatues.value?.set(book.bookID, APILoadingStatus.LOADING)

        val id = book.bookID

        coroutineScope.launch {
            val result = repository.getChaptersIn(book)

            _latestChapters.value?.set(id, when(result) {
                is Result.Success -> {
                    _chapterStatues.value?.set(id, APILoadingStatus.DONE)
                    if(result.data.count() > 0) {
                        result.data.sortedByDescending { it.updatedTime }.first()
                    } else {
                        null
                    }
                }

                is Result.Error -> {
                    _chapterStatues.value?.set(id, APILoadingStatus.ERROR)
                    null
                }

                is Result.Fail -> {
                    _chapterStatues.value?.set(id, APILoadingStatus.ERROR)
                    null
                }

                else -> {
                    _chapterStatues.value?.set(id, APILoadingStatus.ERROR)
                    null
                }
            })

            _latestChapters.value = _latestChapters.value
        }
    }

    private fun fetchNumberOfFollowersFor(book: Book) {
        val id = book.bookID
        _followingStatuses.value?.set(id, APILoadingStatus.LOADING)

        coroutineScope.launch {
            val result = repository.getFollowersForBook(book)

            _numbersOfFollowers.value?.set(id, when(result) {
                is Result.Success -> {
                    _followingStatuses.value?.set(id, APILoadingStatus.DONE)
                    result.data.size
                }

                is Result.Error -> {
                    _followingStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }

                is Result.Fail -> {
                    _followingStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }

                else -> {
                    _followingStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }
            })

            _numbersOfFollowers.value = _numbersOfFollowers.value

        }
    }
}