package studio.saladjam.iwanttobenovelist.factories.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.saladjam.iwanttobenovelist.readerscene.ReaderMixerViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class ChpaterViewModelFactory(private val repository: Repository,
                              private val book: Book?,
                              private val chapter: Chapter)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                else -> throw IllegalArgumentException("Unknown class for BookViewModelFactory")
            }
        } as T
    }
}