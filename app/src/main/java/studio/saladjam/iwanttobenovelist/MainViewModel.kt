package studio.saladjam.iwanttobenovelist

import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class MainViewModel: ViewModel() {
    /***
     * LIVE DATAs and Corresponding ACTIONS
     */

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

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
//    private val _selectedBook = MutableLiveData<Book>()
//    val selectedBook: LiveData<Book>
//        get() = _selectedBook
//
//    fun selectBook(book: Book) {
//        _selectedBook.value = book
//    }
//
//    fun doneNavigateToBook() {
//        _selectedBook.value = null
//    }


    /** NAVIGATE to BOOK DETAIL --- DIVIDED into WRITER and READER VERSION */
    // WRITER
    private val _selectBookToEdit = MutableLiveData<Book>()
    val selectBookToEdit: LiveData<Book>
        get() = _selectBookToEdit

    fun displayEditingBook(book: Book) {
        _selectBookToEdit.value = book
    }

    fun doneDisplayingEditingBook() {
        _selectBookToEdit.value = null
    }

    /** NAVIGATE to EDITOR with CHAPTER */
    private val _selectedChapterForEditing = MutableLiveData<Chapter>()
    val selectedChapterForEditing: LiveData<Chapter>
        get() = _selectedChapterForEditing

    fun selectChapterToEdit(chapter: Chapter) {
        _selectedChapterForEditing.value = chapter
    }

    fun doneNavigateToEditor() {
        _selectedChapterForEditing.value = null
    }


    /** NAVIGATE to MODIFICATION PAGE (adding Images) with Chapter and Paint */
    private val _selectedChapterForModification = MutableLiveData<Chapter>()
    val selectedChapterForModifcation: LiveData<Chapter>
        get() = _selectedChapterForModification

    private val _modificationBasicPaint = MutableLiveData<Paint>()
    val modificationBasicPaint: LiveData<Paint>
        get() = _modificationBasicPaint

    fun navigateToModify(chapter: Chapter, paint: Paint) {
        _selectedChapterForModification.value = chapter
        _modificationBasicPaint.value = paint
    }

    fun doneNavigationToModify() {
        _modificationBasicPaint.value = null
        _selectedChapterForModification.value = null
    }


    /** READER NAVIGATION */
    /** NAVIGATE TO BOOK */
    private val _selectedBookToRead = MutableLiveData<Book>()
    val selectedBookToRead: LiveData<Book>
        get() = _selectedBookToRead

    fun selectedBookToRead(book: Book) {
        _selectedBookToRead.value = book
    }

    fun doneDisplayingReadingBook() {
        _selectedBookToRead.value = null
    }

    /** NAVIGATE TO CHAPTER */
    private val _selectedChapterToRead = MutableLiveData<Pair<Book, Chapter>>()
    val selectedChapterToRead: LiveData<Pair<Book, Chapter>>
        get() = _selectedChapterToRead

    fun selectChpaterToReadInBook(pair: Pair<Book, Chapter>) {
        _selectedChapterToRead.value = pair
    }

    fun doneSelectChapterToRead() {
        _selectedChapterToRead.value = null
    }


}