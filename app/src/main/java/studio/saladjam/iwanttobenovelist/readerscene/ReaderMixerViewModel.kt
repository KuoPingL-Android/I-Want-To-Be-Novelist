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
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class ReaderMixerViewModel (private val repository: Repository,
                            private val book: Book): ViewModel() {

    /** NETWORK */
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
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

    private val _chapterBlocks = MutableLiveData<MutableMap<String, List<ImageBlockRecorder>>>()
    val chapterBlocks: LiveData<MutableMap<String, List<ImageBlockRecorder>>>
        get() = _chapterBlocks

    private val _currentChapterBlock = MutableLiveData<List<ImageBlockRecorder>>()
    val currentChapterBlock: LiveData<List<ImageBlockRecorder>>
        get() = _currentChapterBlock

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

        if (chapterIndex < 0) return

        _status.value = ApiLoadingStatus.LOADING
        val chapterOfInterest = _chapters.filter { it.chapterIndex == chapterIndex }

        if (chapterOfInterest.isNotEmpty()) {

            displayChapter(chapterOfInterest.first())

            if (_chapterBlocks.value?.keys?.contains(chapterOfInterest.first().chapterID) == true) {
                _currentChapterBlock.value = _chapterBlocks.value!![chapterOfInterest.first().chapterID]
                _status.value = ApiLoadingStatus.DONE
                return
            }
        }

        coroutineScope.launch {

            when(val result
                    = repository.getChapterWithDetails(chapterIndex, book.bookID)) {
                is Result.Success -> {
                    val chapter = result.data.first
                    displayChapter(chapter)
                    _chapterBlocks.value?.put(chapter.chapterID, result.data.second)
                    _currentChapterBlock.value = result.data.second
                    _status.value = ApiLoadingStatus.DONE
                    _error.value = null
                }

                is Result.Fail -> {
                    _currentChapterBlock.value = null
                    _status.value = ApiLoadingStatus.ERROR
                    _error.value = result.error
                }

                is Result.Error -> {
                    _currentChapterBlock.value = null
                    _status.value = ApiLoadingStatus.ERROR
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

    fun triggerLikeButton() {

        _currentChapter.value?.let {chapter ->
            val likeChapter = doLikeChapter.value ?: false

            _status.value = ApiLoadingStatus.LOADING
            _error.value = null

            if (likeChapter) {
                coroutineScope.launch {
                    when(val result = repository.postDislikeChapter(chapter)) {
                        is Result.Success -> {
                            doLikeChapter.value = false
                            _error.value = null
                            _status.value = ApiLoadingStatus.DONE
                            numberOfLikes.value = numberOfLikes.value?.minus(1)
                        }

                        is Result.Error -> {
                            _error.value = result.exception.localizedMessage
                            _status.value = ApiLoadingStatus.ERROR
                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = ApiLoadingStatus.ERROR
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    when(val result = repository.postLikeChapter(chapter)) {
                        is Result.Success -> {
                            doLikeChapter.value = true
                            _error.value = null
                            _status.value = ApiLoadingStatus.DONE
                            numberOfLikes.value = numberOfLikes.value?.plus(1)
                        }

                        is Result.Error -> {
                            _error.value = result.exception.localizedMessage
                            _status.value = ApiLoadingStatus.ERROR
                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = ApiLoadingStatus.ERROR
                        }
                    }
                }
            }
        }

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
                        _status.value = ApiLoadingStatus.ERROR
                        doLikeChapter.value = false
                        numberOfLikes.value = 0
                    }

                    is Result.Fail -> {
                        _error.value = result.error
                        _status.value = ApiLoadingStatus.ERROR
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

    fun navigateToNextChapter() {

        if (_status.value == ApiLoadingStatus.LOADING) return

        _status.value = ApiLoadingStatus.LOADING

        if (_currentChapter.value == null)
        {
            _status.value = ApiLoadingStatus.DONE
            return
        }

        _currentChapter.value?.let {
            fetchChapterDetails(it.chapterIndex + 1)
        }

    }

    /** PREVIOUS CHAPTER */
    fun navigateToPreviousChapter() {
        if (_status.value == ApiLoadingStatus.LOADING) return

        _status.value = ApiLoadingStatus.LOADING

        if (_currentChapter.value == null)
        {
            _status.value = ApiLoadingStatus.DONE
            return
        }

        _currentChapter.value?.let {
            fetchChapterDetails(it.chapterIndex - 1)
        }
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