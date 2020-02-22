package studio.saladjam.iwanttobenovelist.homescene

import androidx.lifecycle.*
import com.google.android.gms.common.api.ApiException
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

    private val _numbersOfLikes = MutableLiveData<MutableMap<String, Int>>()
        .apply {
            value = mutableMapOf()
        }
            val numbersOfLikes: LiveData<MutableMap<String, Int>>
        get() = _numbersOfLikes



    private val _chapterStatues = MutableLiveData<MutableMap<String, APILoadingStatus>>()
        .apply {
            value = mutableMapOf()
        }
    val chapterStatues: LiveData<MutableMap<String, APILoadingStatus>>
        get() = _chapterStatues

    private val _likesStatuses = MutableLiveData<MutableMap<String, APILoadingStatus>>()
        .apply {
            value = mutableMapOf()
        }
    val likesStatuses: LiveData<MutableMap<String, APILoadingStatus>>
        get() = _likesStatuses

    val statuses = MediatorLiveData<MutableMap<String, APILoadingStatus>>().apply {
        addSource(_chapterStatues) {  }
        addSource(_likesStatuses) { }
    }

    private val _errors = MutableLiveData<MutableMap<String, String>>()
        .apply {
            value = mutableMapOf()
        }
    val errors: LiveData<MutableMap<String, String>>
        get() = _errors


    private val _books = mutableMapOf<String, Book>()
    private val _bookIDs = mutableListOf<String>()

    fun addBook(book: Book) {
        if (_books.filter { it.key == book.bookID }.isNotEmpty()) {
            return
        }

        _books[book.bookID] = book

        fetchLatestChapterFor(book)
        fetchNumberOfLikesFor(book)
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

    private fun fetchNumberOfLikesFor(book: Book) {
        val id = book.bookID
        _likesStatuses.value?.set(id, APILoadingStatus.LOADING)

        coroutineScope.launch {
            val result = repository.getLikesForBook(book)

            _numbersOfLikes.value?.set(id, when(result) {
                is Result.Success -> {
                    _likesStatuses.value?.set(id, APILoadingStatus.DONE)
                    result.data.size
                }

                is Result.Error -> {
                    _likesStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }

                is Result.Fail -> {
                    _likesStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }

                else -> {
                    _likesStatuses.value?.set(id, APILoadingStatus.ERROR)
                    0
                }
            })

            _numbersOfLikes.value = _numbersOfLikes.value

        }
    }
}