package studio.saladjam.iwanttobenovelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class MainViewModel: ViewModel() {
    /***
     * LIVE DATAs and Corresponding ACTIONS
     */

    /** NAVIGATE to LOGIN PAGE */
    private val _shouldNavigateToLoginPage = MutableLiveData<Boolean>()
    val shouldNavigateToLoginPage: LiveData<Boolean>
        get() = _shouldNavigateToLoginPage

    fun navigateToLoginPage() {
        _shouldNavigateToLoginPage.value = true
    }

    fun doneNavigateToLoginPage() {
        _shouldNavigateToLoginPage.value = null
    }

    /** NAVIGATE to HOME PAGE */
    private val _shouldNavigateToHomePage = MutableLiveData<Boolean>()
    val shouldNavigateToHomePage: LiveData<Boolean>
        get() = _shouldNavigateToHomePage

    fun navigateToHomePage() {
        _shouldNavigateToHomePage.value = true
    }

    fun doneNavigateToHomePage() {
        _shouldNavigateToHomePage.value = null
    }

    /** NAVIGATE to CATEGORY SCENE */
    private val _shouldNavigateToCategoryPage = MutableLiveData<Boolean>()
    val shouldNavigateToCategoryPage: LiveData<Boolean>
        get() = _shouldNavigateToCategoryPage

    fun navigateToCategory() {
        _shouldNavigateToCategoryPage.value = true
    }

    fun doneNavigateToCategory() {
        _shouldNavigateToCategoryPage.value = null
    }

    /** NAVIGATE to PROFILE SCENE */
    private val _shouldNavigateToProfilePage = MutableLiveData<Boolean>()
    val shouldNavigateToProfilePage: LiveData<Boolean>
        get() = _shouldNavigateToProfilePage

    fun navigateToProfilePage() {
        _shouldNavigateToProfilePage.value = true
    }

    fun doneNavigateToProfilePage() {
        _shouldNavigateToProfilePage.value = null
    }

    /** NAVIGATE to BOOK */
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