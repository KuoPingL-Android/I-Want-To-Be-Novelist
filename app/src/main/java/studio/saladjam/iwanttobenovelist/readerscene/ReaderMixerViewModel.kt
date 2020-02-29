package studio.saladjam.iwanttobenovelist.readerscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.ImageBlockRecorder
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class ReaderMixerViewModel (private val repository: Repository,
                            private val book: Book): ViewModel() {
    /** NETWORK */
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** ERROR */
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** DATA SETUP */
    private val _currentChapter = MutableLiveData<Chapter>()
    val currentChapter: LiveData<Chapter>
        get() = _currentChapter

    private val _chapters = mutableListOf<Chapter>()
    private val _chaptersDetails = mutableMapOf<Chapter, MutableList<ImageBlockRecorder>>()

    private val _chapterBlocks = MutableLiveData<List<ImageBlockRecorder>>()
    val chapterBlocks: LiveData<List<ImageBlockRecorder>>
        get() = _chapterBlocks

    fun displayChapter(chapter: Chapter) {
        if (!_chapters.contains(chapter)) {
            _chapters.add(chapter)
            _chapters.sortBy { it.chapterIndex }
            _chaptersDetails[chapter] = mutableListOf()
        }

        _currentChapter.value = chapter
        _isFirstChapter.value = chapter.chapterIndex == 0
        _currentIndex.value = "${chapter.chapterIndex + 1} / ${book.chapterCount}"
        _isLastChapter.value = chapter.chapterIndex == book.chapterCount - 1

        checkIfLikeChapter()
    }

    fun fetchChapterDetails(chapterIndex: Int) {

        _status.value = APILoadingStatus.LOADING

        coroutineScope.launch {
            val result = repository.getChapterWithDetails(chapterIndex, book)

            when(result) {
                is Result.Success -> {
                    displayChapter(result.data.first)
                    _chapterBlocks.value = result.data.second
                    _status.value = APILoadingStatus.DONE
                    _error.value = null
                }

                is Result.Fail -> {
                    _chapterBlocks.value = null
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.error
                }

                is Result.Error -> {
                    _chapterBlocks.value = null
                    _status.value = APILoadingStatus.ERROR
                    _error.value = result.exception.localizedMessage
                }
            }
        }
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

        _currentChapter.value?.let {chapter ->
            val likeChapter = doLikeChapter.value ?: false

            _status.value = APILoadingStatus.LOADING
            _error.value = null

            if (likeChapter) {
                coroutineScope.launch {
                    when(val result = repository.postDislikeChapter(chapter)) {
                        is Result.Success -> {
                            doLikeChapter.value = false
                            _error.value = null
                            _status.value = APILoadingStatus.DONE
                            numberOfLikes.value = numberOfLikes.value?.minus(1)
                        }

                        is Result.Error -> {
                            _error.value = result.exception.localizedMessage
                            _status.value = APILoadingStatus.ERROR
                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = APILoadingStatus.ERROR
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    when(val result = repository.postLikeChapter(chapter)) {
                        is Result.Success -> {
                            doLikeChapter.value = true
                            _error.value = null
                            _status.value = APILoadingStatus.DONE
                            numberOfLikes.value = numberOfLikes.value?.plus(1)
                        }

                        is Result.Error -> {
                            _error.value = result.exception.localizedMessage
                            _status.value = APILoadingStatus.ERROR
                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = APILoadingStatus.ERROR
                        }
                    }
                }
            }
        }

    }

    fun doneTriggeringLikeButton() {
        _shouldLikeOrUnlikeTheChapter.value = null
    }

    val doLikeChapter = MutableLiveData<Boolean>()
    val numberOfLikes = MutableLiveData<Int>()

    private fun checkIfLikeChapter() {
        _currentChapter.value?.let {chapter ->
            coroutineScope.launch {
                when(val result = repository.getLikesForChapter(chapter)) {
                    is Result.Success -> {
                        doLikeChapter.value = result.data.contains(IWBNApplication.user.userID)
                        numberOfLikes.value = result.data.size
                    }

                    is Result.Error -> {
                        _error.value = result.exception.localizedMessage
                        _status.value = APILoadingStatus.ERROR
                        doLikeChapter.value = false
                        numberOfLikes.value = 0
                    }

                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = APILoadingStatus.ERROR
                        doLikeChapter.value = false
                        numberOfLikes.value = 0
                    }
                }
            }
        }
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

    /** NEXT CHAPTER */
    private val _shouldNavigateToNextChapter = MutableLiveData<Boolean>()
    val shouldNavigateToNextChapter: LiveData<Boolean>
        get() = _shouldNavigateToNextChapter

    fun navigateToNextChapter() {
        //TODO: FETCH NEXT CHAPTER instead of changing these variables
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