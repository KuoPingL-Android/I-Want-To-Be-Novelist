package studio.saladjam.iwanttobenovelist.bookscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.Section

class BookViewModel(private val repository: Repository): ViewModel() {
    private val _selectedChapter = MutableLiveData<Chapter>()
    val selectedChapter: LiveData<Chapter>
        get() = _selectedChapter

    private val _selectedSection = MutableLiveData<Section>()
    val selectedSection: LiveData<Section>
        get() = _selectedSection

    fun doneDisplayingSection() {
        _selectedSection.value = null
    }

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book>
        get() = _book

    fun setBook(book: Book) {
        _book.value = book
    }
}