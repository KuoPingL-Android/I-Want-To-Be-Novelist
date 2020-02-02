package studio.saladjam.iwanttobenovelist.categoryscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class CategoryListViewModel (private val repository: Repository): ViewModel() {
    /** BOOKS */
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    /** REPOSITORY */
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun fetchBooksOn(genre: Genre, filter: String) {
        //TODO: TAKE INTO ACCOUNT OF FILTER
        coroutineScope.launch {

            val result = repository.getBooks(genre.id)
            when(result) {
                is Result.Success -> {
                    _books.value = result.data
                }

                is Result.Fail -> {
                    result.error
                }

                is Result.Error -> {
                    result.exception
                }
            }
        }
    }

    /** SELECT ACTION */
    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun doneNavigateToBook() {
        _selectedBook.value = null
    }
}