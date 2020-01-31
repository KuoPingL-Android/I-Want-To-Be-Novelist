package studio.saladjam.iwanttobenovelist.homescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
import studio.saladjam.iwanttobenovelist.repository.dataclass.Roles

class HomeViewModel(val repository: Repository): ViewModel() {

    /** DATA NEEDED TO BE DISPLAYED */
    private val _recommendedList = MutableLiveData<List<Book>>()
    val recommendedList: LiveData<List<Book>>
        get() = _recommendedList

    private val _myWorkList = MutableLiveData<List<Book>>()
    val myWorkList: LiveData<List<Book>>
        get() = _myWorkList

    private val _myFollowList = MutableLiveData<List<Book>>()
    val myFollowList: LiveData<List<Book>>
        get() = _myFollowList

    private val _onlyShowMostPopularBooks =
        MediatorLiveData<Boolean>().apply {
        addSource(_myFollowList){checkAvailableLists()}
        addSource(_myWorkList){checkAvailableLists()}
    }
    val onlyShowMostPopularBooks: LiveData<Boolean>
        get() = _onlyShowMostPopularBooks

    private fun checkAvailableLists() {
        _onlyShowMostPopularBooks.value = (_myWorkList.value?.isEmpty() == true || user.token == null)
    }

    /** REPOSITORY RELATED ACTIONS */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val user = IWBNApplication.user

    fun fetchDatas() {

        fetchRecommendedList()
        when(user.role) {
            Roles.WRITER.value -> {
                fetchMyWorkList()
            }
        }

        if(user.token == null || user.bookFollowees.count() == 0) {
            // VISITOR
            checkAvailableLists()
            fetchMostPopularBooks()
        } else {
            fetchMyFollowList()
        }
    }

    /** RECOMMENDED LIST for EVERYONE */
    private fun fetchRecommendedList() {
        coroutineScope.launch {
            val result = repository.getUserRecommendedList(user)
            when(result) {
                is Result.Success -> {
                    _recommendedList.value = result.data
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    result.error
                }
            }
        }
    }

    /** WRITER's LIST of WORK */
    private fun fetchMyWorkList() {
        coroutineScope.launch {
            val result = repository.getUserWork(user)
            when(result) {
                is Result.Success -> {
                    _myWorkList.value = result.data
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    result.error
                }
            }
        }
    }

    /** WRITER or REVIEWER's LIST they FOLLOW */
    private fun fetchMyFollowList() {
        coroutineScope.launch {
            val result = repository.getUserFollowing(user)
            when(result) {
                is Result.Success -> {
                    _myFollowList.value = result.data
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    result.error
                }
            }
        }
    }

    /** VISITOR's LIST of BOOKs */
    private fun fetchMostPopularBooks() {
        coroutineScope.launch {
            val result = repository.getMostPopularBooks()
            when(result) {
                is Result.Success -> {
                    _myFollowList.value = result.data
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    result.error
                }
            }
        }
    }


    /** NAVIGATE to BOOK CONTENT */
    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun doneSelectingBook() {
        _selectedBook.value = null
    }

    /** NAVIGATE to MY FOLLOWING LIST */
    private val _shouldNavigateToMyFollow = MutableLiveData<Boolean>()
    val shouldNavigateToMyFollow: LiveData<Boolean>
        get() = _shouldNavigateToMyFollow

    fun navigateToMyFollow() {
        _shouldNavigateToMyFollow.value = true
    }

    fun doneNavigateToMyFollow() {
        _shouldNavigateToMyFollow.value = null
    }

    /** NAVIGATE to MY WORK */
    private val _shouldNavigateToMyWork = MutableLiveData<Boolean>()
    val shouldNavigateToMyWork: LiveData<Boolean>
        get() = _shouldNavigateToMyWork

    fun navigateToMyWork() {
        _shouldNavigateToMyWork.value = true
    }

    fun doneNavigateToMyWork() {
        _shouldNavigateToMyWork.value = null
    }
}