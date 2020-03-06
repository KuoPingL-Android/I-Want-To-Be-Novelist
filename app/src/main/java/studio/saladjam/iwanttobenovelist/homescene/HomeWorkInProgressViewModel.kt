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
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class HomeWorkInProgressViewModel(private val repository: Repository) : ViewModel() {

    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** DATAs */
    private val _latestChapters =
        MutableLiveData<MutableMap<String, Chapter?>>().apply {
        value = mutableMapOf()
    }

    val latestChapters: LiveData<MutableMap<String, Chapter?>>
        get() = _latestChapters

    private val _numbersOfFollowers =
        MutableLiveData<MutableMap<String, Int>>().apply {
            value = mutableMapOf()
        }

    val numbersOfFollowers: LiveData<MutableMap<String, Int>>
        get() = _numbersOfFollowers


    private val _chapterStatues =
        MutableLiveData<MutableMap<String, ApiLoadingStatus>>().apply {
            value = mutableMapOf()
        }

    private val _followingStatuses =
        MutableLiveData<MutableMap<String, ApiLoadingStatus>>().apply {
            value = mutableMapOf()
        }

    private val _errors =
        MutableLiveData<MutableMap<String, String>>().apply {
            value = mutableMapOf()
        }


    private val _books = mutableMapOf<String, Book>()

    fun addBook(book: Book) {
        if (_books.filter { it.key == book.bookID }.isNotEmpty()) {
            return
        }

        _books[book.bookID] = book
        fetchLatestChapterFor(book)
        fetchNumberOfFollowersFor(book)
    }

    private fun fetchLatestChapterFor(book: Book) {
        _chapterStatues.value?.set(book.bookID, ApiLoadingStatus.LOADING)

        val id = book.bookID

        coroutineScope.launch {
            _latestChapters.value?.set(id,
                when (val result = repository.getChaptersIn(book)) {
                    is Result.Success -> {
                        _chapterStatues.value?.set(id, ApiLoadingStatus.DONE)
                        result.data.maxBy { it.updatedTime }
                    }

                    is Result.Error -> {
                        _chapterStatues.value?.set(id, ApiLoadingStatus.ERROR)
                        null
                    }

                    is Result.Fail -> {
                        _chapterStatues.value?.set(id, ApiLoadingStatus.ERROR)
                        null
                    }

                    else -> {
                        _chapterStatues.value?.set(id, ApiLoadingStatus.ERROR)
                        null
                    }
                })

            _latestChapters.value = _latestChapters.value
        }
    }

    private fun fetchNumberOfFollowersFor(book: Book) {
        val id = book.bookID
        _followingStatuses.value?.set(id, ApiLoadingStatus.LOADING)

        coroutineScope.launch {

            _numbersOfFollowers.value?.set(
                id,
                when (val result = repository.getFollowersForBook(book)) {
                    is Result.Success -> {
                        _followingStatuses.value?.set(id, ApiLoadingStatus.DONE)
                        result.data.size
                    }

                    is Result.Error -> {
                        _followingStatuses.value?.set(id, ApiLoadingStatus.ERROR)
                        0
                    }

                    is Result.Fail -> {
                        _followingStatuses.value?.set(id, ApiLoadingStatus.ERROR)
                        0
                    }

                    else -> {
                        _followingStatuses.value?.set(id, ApiLoadingStatus.ERROR)
                        0
                    }
                }
            )

            _numbersOfFollowers.value = _numbersOfFollowers.value

        }
    }
}