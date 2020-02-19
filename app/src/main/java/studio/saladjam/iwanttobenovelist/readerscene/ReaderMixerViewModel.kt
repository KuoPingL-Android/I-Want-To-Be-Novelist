package studio.saladjam.iwanttobenovelist.readerscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class ReaderMixerViewModel (private val repository: Repository,
                            private val book: Book): ViewModel() {
    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** DATA SETUP */
    private val _currentChapter = MutableLiveData<Chapter>()
    val currentChapter: LiveData<Chapter>
        get() = _currentChapter

    fun displayChapter(chapter: Chapter) {
        _currentChapter.value = chapter
        _isFirstChapter.value = chapter.chapterIndex == 0
        _currentIndex.value = "${chapter.chapterIndex + 1} / ${book.chapterCount}"
        _isLastChapter.value = chapter.chapterIndex == book.chapterCount - 1
    }

    /** UI SETTINGS */
    private val _isFirstChapter = MutableLiveData<Boolean>()
    val isFirstChapter: LiveData<Boolean>
        get() = _isFirstChapter

    private val _currentIndex = MutableLiveData<String>()
    val currentIndex: LiveData<String>
        get() = _currentIndex

    private val _isLastChapter = MutableLiveData<Boolean>()
    val isLastChapter: LiveData<Boolean>
        get() = _isLastChapter


    /** BUTTON ACTIONS */
    /** LIKE */
    private val _shouldLikeOrUnlikeTheChapter = MutableLiveData<Boolean>()
    val shouldLikeOrUnlikeTheChapter: LiveData<Boolean>
        get() = _shouldLikeOrUnlikeTheChapter

    fun triggerLikeButton() {
        _shouldLikeOrUnlikeTheChapter.value = true
    }

    fun doneTriggeringLikeButton() {
        _shouldLikeOrUnlikeTheChapter.value = null
    }

    /** COMMENT */
    private val _shouldShowCommentDialog = MutableLiveData<Boolean>()
    val shouldShowCommentDialog: LiveData<Boolean>
        get() = _shouldShowCommentDialog

    fun showComments() {
        _shouldShowCommentDialog.value = true
    }

    fun doneShowingCommentDialog() {
        _shouldShowCommentDialog.value = null
    }

    /** NEXT CHAPTER*/
    private val _shouldNavigateToNextChapter = MutableLiveData<Boolean>()
    val shouldNavigateToNextChapter: LiveData<Boolean>
        get() = _shouldNavigateToNextChapter

    fun navigateToNextChapter() {
        _shouldNavigateToNextChapter.value = true
    }

    fun doneNavigateToNextChapter() {
        _shouldNavigateToNextChapter.value = null
    }

    /** PREVIOUS CHAPTER */
    private val _shouldNavigateToPreviousChapter = MutableLiveData<Boolean>()
    val shouldNavigateToPreviousChapter: LiveData<Boolean>
        get() = _shouldNavigateToPreviousChapter

    fun navigateToPreviousChapter() {
        _shouldNavigateToPreviousChapter.value = true
    }

    fun doneNavigateToPreviousChapter() {
        _shouldNavigateToPreviousChapter.value = null
    }

    /** BACK */
    private val _shouldNavigateBackToPreviousPage = MutableLiveData<Boolean>()
    val shouldNavigateBackToPreviousPage: LiveData<Boolean>
        get() = _shouldNavigateBackToPreviousPage

    fun navigateBackToPreviousPage() {
        _shouldNavigateBackToPreviousPage.value = true
    }

    fun doneNavigateToPreviousPage() {
        _shouldNavigateBackToPreviousPage.value = null
    }


}