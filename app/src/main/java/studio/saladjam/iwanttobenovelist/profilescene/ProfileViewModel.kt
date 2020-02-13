package studio.saladjam.iwanttobenovelist.profilescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

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

    private val _user = MutableLiveData<User>().apply {
        value = IWBNApplication.user
    }
    val user: LiveData<User>
        get() = _user

    /** CREATE NEW BOOK */
    private val _shouldCreateNewBook = MutableLiveData<Boolean>()
    val shouldCreateNewBook: LiveData<Boolean>
        get() = _shouldCreateNewBook

    fun createNewBook() {
        _shouldCreateNewBook.value = true
    }

    fun doneNavigateToNewBook() {
        _shouldCreateNewBook.value = null
    }

    /** SELECT BOOK ACTION */
    private val _bookSelectedToEdit = MutableLiveData<Book>()
    val bookSelectedToEdit: LiveData<Book>
        get() = _bookSelectedToEdit

    fun selectBookForEdit(book: Book) {
        _bookSelectedToEdit.value = book
    }

    fun doneNavigateToEditBook() {
        _bookSelectedToEdit.value = null
    }

    private val _bookSelectedToRead = MutableLiveData<Book>()
    val bookSelectedToRead: LiveData<Book>
        get() = _bookSelectedToRead

    fun selectBookForRead(book: Book) {
        _bookSelectedToRead.value = book
    }

    fun doneNavigateToReadBook() {
        _bookSelectedToRead.value = null
    }




}