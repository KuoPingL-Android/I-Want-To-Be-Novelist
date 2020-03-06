package studio.saladjam.iwanttobenovelist.homescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Roles

class HomeViewModel(val repository: Repository): ViewModel() {

    /** DATA NEEDED TO BE DISPLAYED */
    private val _recommendedList = MutableLiveData<List<Book>>()

    private val _popularList = MutableLiveData<List<Book>>()

    private val _myWorkList = MutableLiveData<List<Book>>()

    private val _myFollowList = MutableLiveData<List<Book>>()

    private val _onlyShowMostPopularBooks =
        MediatorLiveData<Boolean>().apply {
        addSource(_myFollowList){checkAvailableLists()}
        addSource(_myWorkList){checkAvailableLists()}
    }

    val onlyShowMostPopularBooks: LiveData<Boolean>
        get() = _onlyShowMostPopularBooks

    private fun checkAvailableLists() {
        _onlyShowMostPopularBooks.value =
            (_myWorkList.value?.isEmpty() == true || user.token == null)
    }

    private val _finalList = MutableLiveData<List<HomeSealItems>>()
    val finaList: LiveData<List<HomeSealItems>>
        get() = _finalList


    private val _areDataReady = MediatorLiveData<Boolean>().apply {
        addSource(_recommendedList) {checkIfListAreReady()}
        addSource(_myFollowList){checkIfListAreReady()}
        addSource(_myWorkList){checkIfListAreReady()}
        addSource(_popularList){checkIfListAreReady()}
    }
    val areDataRead: LiveData<Boolean>
        get() = _areDataReady

    private fun checkIfListAreReady() {
        if (user.token != null) {
            _areDataReady.value =
                (_recommendedList.value != null &&
                        _myFollowList.value != null &&
                        (user.role == Roles.REVIEWER.value || _myWorkList.value != null))
        } else {
            _areDataReady.value =
                (_recommendedList.value != null &&
                        _popularList.value != null &&
                        (user.role == Roles.REVIEWER.value || _myWorkList.value != null))
        }
    }

    fun prepareFinalList() {

        val list = mutableListOf<HomeSealItems>()

        if (_recommendedList.value != null) {
            list.add(
                HomeSealItems.
                    Recommend(_recommendedList.value!!, HomeSections.RECOMMEND))
        }

        if (!_myFollowList.value.isNullOrEmpty()) {
            if (user.token != null) {
                list.add(HomeSealItems.
                    CurrentReading(_myFollowList.value!!, HomeSections.CURRENT_READING))
            }
        }

        if (!_popularList.value.isNullOrEmpty()) {
            list.add(HomeSealItems.
                General(_popularList.value!!, HomeSections.POPULAR))
        }

        if (!_myWorkList.value.isNullOrEmpty()) {
            list.add(HomeSealItems.
                WorkInProgress(_myWorkList.value!!, HomeSections.WORK_IN_PROGRESS))
        }

        _finalList.value = list
    }


    /** REPOSITORY RELATED ACTIONS */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val user = IWBNApplication.user

    fun fetchDatas() {
        fetchRecommendedList()
        fetchMyWorkList()

        if(user.token == null) {
            // VISITOR
            checkAvailableLists()
        } else {
            fetchMyFollowList()
        }
        fetchMostPopularBooks()
    }

    /** RECOMMENDED LIST for EVERYONE */
    private fun fetchRecommendedList() {
        coroutineScope.launch {
            when(val result = repository.getUserRecommendedList(user)) {
                is Result.Success -> {
                    _recommendedList.value = result.data
                }

                is Result.Error -> {
                    _recommendedList.value = listOf()
                    result.exception
                }

                is Result.Fail -> {
                    _recommendedList.value = listOf()
                    result.error
                }
            }
        }
    }

    /** WRITER's LIST of WORK */
    private fun fetchMyWorkList() {
        coroutineScope.launch {

            when(val result = repository.getUserWork(user)) {
                is Result.Success -> {
                    _myWorkList.value = result.data
                }

                is Result.Error -> {
                    _myFollowList.value = listOf()
                    result.exception
                }

                is Result.Fail -> {
                    _myFollowList.value = listOf()
                    result.error
                }
            }
        }
    }

    /** WRITER or REVIEWER's LIST they FOLLOW */

    private fun fetchMyFollowList() {
        repository.addBooksFollowingSnapshotListener(user.userID) {
            coroutineScope.launch {

                when(val result = repository.getFollowingBooks(it)) {
                    is Result.Success -> {
                        _myFollowList.value = result.data
                    }

                    is Result.Error -> {
                        _myFollowList.value = listOf()
                        result.exception
                    }

                    is Result.Fail -> {
                        _myFollowList.value = listOf()
                        result.error
                    }
                }
            }
        }
    }

    /** VISITOR's LIST of BOOKs */
    private fun fetchMostPopularBooks() {
        coroutineScope.launch {

            when(val result = repository.getMostPopularBooks()) {
                is Result.Success -> {
                    _popularList.value = result.data
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

    /** SEE ALL is PRESSED */
    fun pressedSeeAllOn(homeSection: String) {
        when(homeSection) {
            HomeSections.RECOMMEND.id -> {
                Logger.i("NAVIGATE TO RECOMMEND")
                _shouldNavigateToRecommend.value = true
            }

            HomeSections.CURRENT_READING.id -> {
                Logger.i("NAVIGATE TO CURRENT READ")
                _shouldNavigateToMyFollow.value = true
            }

            HomeSections.WORK_IN_PROGRESS.id -> {
                Logger.i("NAVIGATE TO WORK IN PROGRESS")
                _shouldNavigateToMyWork.value = true
            }

            HomeSections.POPULAR.id -> {
                _shouldNavigateToPopular.value = true
            }
        }
    }


    /** NAVIGATE to BOOK CONTENT */
    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    private val _selectedWork = MutableLiveData<Book>()
    val selectedWork: LiveData<Book>
        get() = _selectedWork

    fun selectBook(book: Book, section: HomeSections) {
        when(section) {
            HomeSections.WORK_IN_PROGRESS -> {
                _selectedWork.value = book
            }

            else -> {
                selectBook(book)
            }
        }
    }

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun doneSelectingBook() {
        _selectedBook.value = null
    }

    fun doneSelectingWork() {
        _selectedWork.value = null
    }

    /** NAVIGATE to CURRENT READING */
    private val _shouldNavigateToMyFollow = MutableLiveData<Boolean>()
    val shouldNavigateToMyFollow: LiveData<Boolean>
        get() = _shouldNavigateToMyFollow
    fun doneNavigateToMyFollow() {
        _shouldNavigateToMyFollow.value = null
    }

    /** NAVIGATE to WORK IN PROGRESS */
    private val _shouldNavigateToMyWork = MutableLiveData<Boolean>()
    val shouldNavigateToMyWork: LiveData<Boolean>
        get() = _shouldNavigateToMyWork

    fun doneNavigateToMyWork() {
        _shouldNavigateToMyWork.value = null
    }

    /** NAVIGATE to RECOMMEND */
    private val _shouldNavigateToRecommend = MutableLiveData<Boolean>()
    val shouldNavigateToRecommend: LiveData<Boolean>
        get() = _shouldNavigateToRecommend

    fun doneNavigateToRecommend() {
        _shouldNavigateToRecommend.value = null
    }

    /** NAVIGATE to POPULAR */
    private val _shouldNavigateToPopular = MutableLiveData<Boolean>()
    val shouldNavigateToPopular: LiveData<Boolean>
        get() = _shouldNavigateToPopular

    fun doneNavigateToPopular() {
        _shouldNavigateToPopular.value = null
    }
}