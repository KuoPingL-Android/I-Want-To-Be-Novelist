package studio.saladjam.iwanttobenovelist.searchscene

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class SearchViewModel(private val repository: Repository) : ViewModel() {

    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** FILTER SELECTION */
    val filters = MutableLiveData<List<SearchFilters>>().apply {
        value = listOf(
            SearchFilters.POPULARITY,
            SearchFilters.MOST_RECENTLY,
            SearchFilters.RECOMMENDED,
            SearchFilters.COMPLETED
        )
    }

    private val _selectedFilter = MutableLiveData<SearchFilters>().apply {
        // DEFAULT
        value = SearchFilters.POPULARITY
    }
    val selectedFilter: LiveData<SearchFilters>
        get() = _selectedFilter

    fun select(filter: SearchFilters) {
        _selectedFilter.value = filter
        makeSearch()
    }

    /** SEARCH VALUE */
    private val _searchValue = MutableLiveData<String>().apply {
        value = ""
    }
    val searchValue: LiveData<String>
        get() = _searchValue

    fun makeSearchOnText(string: String) {
        _searchValue.value = string
        makeSearch()
    }

    val shouldMakeNewSearch = MediatorLiveData<Boolean>().apply {
        addSource(_searchValue) { value = if(_searchValue.value.isNullOrEmpty()) null else true }
    }

    fun makeSearch() {

        _status.value = APILoadingStatus.LOADING

        coroutineScope.launch {
            val result = repository.getBooksBasedOn(_searchValue.value!!, _selectedFilter.value!!)

            when(result) {
                is Result.Success -> {
                    _status.value = APILoadingStatus.DONE
                    _error.value = null
                    _books.value = result.data
                }

                is Result.Error -> {
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.exception.localizedMessage
                    _books.value = listOf()
                }

                is Result.Fail -> {
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.error
                    _books.value = listOf()
                }
            }
        }
    }

    /**  */
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    private fun fetchResults() {

    }

    /** ACTIONS */
    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun doneSelectingBook() {
        _selectedBook.value = null
    }

}