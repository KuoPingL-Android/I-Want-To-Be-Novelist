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
    val recommendedList: LiveData<List<Book>>
        get() = _recommendedList

    private val _popularList = MutableLiveData<List<Book>>()
    val popularList: LiveData<List<Book>>
        get() = _popularList

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

        if (_finalList.value != null) {return}

        val list = mutableListOf<HomeSealItems>()

        //TODO: FIX ALL LIST to PROPER ONES
        if (_recommendedList.value != null) {
            list.add(HomeSealItems.Recommend("Recommend", _recommendedList.value!!, HomeSections.RECOMMEND))
        }

        if (_myFollowList.value != null && _myFollowList.value!!.isNotEmpty()) {
            if (user.token != null) {
                list.add(HomeSealItems.CurrentReading("Currently Reading", _myFollowList.value!!, HomeSections.CURRENTREAD))
            }
        }

        if (_myWorkList.value != null) {
            list.add(HomeSealItems.WorkInProgress("My Work", _myWorkList.value!!, HomeSections.WORKINPROGRESS))
        }

        if (_popularList.value != null) {
            list.add(HomeSealItems.General("Popular", _popularList.value!!, HomeSections.POPULAR))
        }

        _finalList.value = list
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
            val result = repository.getUserRecommendedList(user)
            when(result) {
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
            val result = repository.getUserWork(user)
            when(result) {
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
        coroutineScope.launch {
            val result = repository.getUserFollowing(user)
            when(result) {
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

    /** VISITOR's LIST of BOOKs */
    private fun fetchMostPopularBooks() {
        coroutineScope.launch {
            val result = repository.getMostPopularBooks()
            when(result) {
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
            HomeSections.RECOMMEND.value -> {
                Logger.i("NAVIGATE TO RECOMMEND")
            }

            HomeSections.CURRENTREAD.value -> {
                Logger.i("NAVIGATE TO CURRENT READ")
                _shouldNavigateToMyFollow.value = true
            }

            HomeSections.WORKINPROGRESS.value -> {
                Logger.i("NAVIGATE TO WORK IN PROGRESS")
                _shouldNavigateToMyWork.value = true
            }

            HomeSections.POPULAR.value -> {

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
            HomeSections.WORKINPROGRESS -> {
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
}