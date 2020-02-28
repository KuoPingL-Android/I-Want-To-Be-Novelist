package studio.saladjam.iwanttobenovelist.bookdetailscene

import android.util.Log
import androidx.databinding.BindingConversion
import androidx.databinding.InverseMethod
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailSealedItem
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus
import java.lang.NumberFormatException

class BookDetailViewModel (private val repository: Repository): ViewModel() {

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


    fun checkBookInfo() {
        fetchChapters()
        checkIfBookIsFollowed()
    }

    /** FETCH ALL CHAPTERS */
    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>>
        get() = _chapters

    private fun fetchChapters() {

        _status.value = APILoadingStatus.LOADING

        if (book == null) {
            _error.value = null
            _chapters.value = null
            _status.value = APILoadingStatus.DONE
            return
        }

        coroutineScope.launch {

            when(val result = repository.getChaptersIn(book!!)) {
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

        // FETCH BOOK TOTAL CHAPTER NUMBERS
    }

    fun donePreparingNewChapter() {
        _shouldAddChapter.value = null
    }

    /** FOLLOW BOOK */
    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean>
        get() = _isFollowing

    val totalFollowers = MutableLiveData<Long>().apply {
        value = 0
    }

    @InverseMethod("stringToLong")
    fun longToString(value: Long): String {
        return value.toString()
    }

    fun stringToLong(value: String): Long {
        return try {
            value.toLong().let {
                it
            }
        } catch (e: NumberFormatException) {
            0L
        }
    }


    private fun checkIfBookIsFollowed() {

        book?.let {book ->
            _status.value = APILoadingStatus.LOADING

            coroutineScope.launch {

                when(val result = repository.getFollowersForBook(book)) {
                    is Result.Success -> {
                        _status.value = APILoadingStatus.DONE
                        _error.value = null
                        totalFollowers.value = result.data.size.toLong()
                        _isFollowing.value = result.data.contains(IWBNApplication.user.userID)
                    }

                    is Result.Fail -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.error
                    }

                    is Result.Error -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                    }
                }
            }
        }
    }

    fun triggerFollowBook() {

        book?.let {book ->
            _status.value = APILoadingStatus.LOADING
            coroutineScope.launch {

                when(val result = repository.updateFollowBook(book)) {
                    is Result.Success -> {
                        _status.value = APILoadingStatus.DONE
                        _error.value = null
                        if (_isFollowing.value == true) {
                            // This means user unfollows it
                            totalFollowers.value = totalFollowers.value?.minus(1)

                            Log.i("TOTAL FOLLOWER", "${totalFollowers.value}")
                        } else {
                            totalFollowers.value = totalFollowers.value?.plus(1)
                        }
                        _isFollowing.value = result.data
                    }

                    is Result.Fail -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.error
                    }

                    is Result.Error -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                    }
                }

            }
        }
    }

    /** EDIT THE SELECTED CHAPTER */
    private val _selectChapterToEdit = MutableLiveData<Chapter>()
    val selectedChapterToEdit: LiveData<Chapter>
        get() = _selectChapterToEdit

    fun editChapter(chapter: Chapter) {
        _selectChapterToEdit.value = chapter
    }

    fun doneNavigatingToChapter() {
        _selectChapterToEdit.value = null
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

    /** FETCH CHAPTERS LIKEs and BOOK FOLLOWERS */
    //TODO: IMPLEMENT

    /** DISPLAY DIALOG to EDIT BOOK INFO */
    private val _shouldShowBookDetailEditor = MutableLiveData<Boolean>()
    val shouldShowBookDetailEditor: LiveData<Boolean>
        get() = _shouldShowBookDetailEditor

    fun editBookDetail() {
        _shouldShowBookDetailEditor.value = true
    }

    fun doneShowingEditorForBookDetail() {
        _shouldShowBookDetailEditor.value = null
    }



    /** NAVIGATE to READER PAGE */
    private val _selectedChapterToRead = MutableLiveData<Chapter>()
    val selectedChapterToRead: LiveData<Chapter>
        get() = _selectedChapterToRead

    fun selectReadingChapter(chapter:Chapter) {
        _selectedChapterToRead.value = chapter
    }

    fun doneNavigateToReader() {
        _selectedChapterToRead.value = null
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