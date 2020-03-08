package studio.saladjam.iwanttobenovelist.bookdetailscene.viewmodel

import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus
import java.lang.NumberFormatException

class ChapterDetailViewModel(private val repository: Repository) : ViewModel() {
    /** NETWORK */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    /** STATUS */
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** CHAPTER INFO */
    val likesForChapters = MutableLiveData<MutableMap<String, Int>>().apply {
        value = mutableMapOf()
    }
    val doesUserLikeChapters =  MutableLiveData<MutableMap<String, Boolean>>()
        .apply {
        value = mutableMapOf()
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun fetchLikesForChapter(chapter: Chapter) {

        _status.value = ApiLoadingStatus.LOADING

        coroutineScope.launch {
            when(val result = repository.getLikesForChapter(chapter)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = ApiLoadingStatus.DONE
                    likesForChapters.value?.put(chapter.chapterID, result.data.size)
                    likesForChapters.value = likesForChapters.value
                    doesUserLikeChapters.value?.put(chapter.chapterID, result.data.contains(IWBNApplication.user.userID))
                    doesUserLikeChapters.value = doesUserLikeChapters.value
                }

                is Result.Fail -> {
                    // DO NOTHING
                    _error.value = result.error
                    _status.value = ApiLoadingStatus.ERROR
                    Logger.e("getLikesForChapter:${result.error}")
                }

                is Result.Error -> {
                    // DO NOTHING
                    _error.value = result.exception.localizedMessage
                    _status.value = ApiLoadingStatus.ERROR
                    Logger.e("getLikesForChapter:${result.exception.localizedMessage}")
                }
            }
        }
    }

    fun getNumberOfLikesForChapter(chapter: Chapter): Int {
        return if (likesForChapters.value?.get(chapter.chapterID) == null) {
            0
        } else {
            likesForChapters.value!![chapter.chapterID]!!
        }
    }

    fun likesFromIntToString(likes: Int): String {
        return likes.toString()
    }

    @InverseMethod("likesFromIntToString")
    fun likesFromStringToInt(value: String): Int {
        return try {
            value.toInt().let {
                it
            }
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun getDoesUserLikeChapter(chapter: Chapter): Boolean {
        return if(doesUserLikeChapters.value?.get(chapter.chapterID) == null) {
            false
        } else {
            doesUserLikeChapters.value!![chapter.chapterID]!!
        }
    }

}