package studio.saladjam.iwanttobenovelist.factories.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.saladjam.iwanttobenovelist.readerscene.ReaderMixerViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

@Suppress("UNCHECKED_CAST")
class BookViewModelFactory(private val repository: Repository, private val book: Book)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(ReaderMixerViewModel::class.java)
                            -> ReaderMixerViewModel(repository, book)
                else -> throw IllegalArgumentException("Unknown class for BookViewModelFactory")
            }
        } as T
    }
}