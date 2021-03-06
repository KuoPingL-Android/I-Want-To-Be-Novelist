package studio.saladjam.iwanttobenovelist.editorscene.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class EditorTextViewModel(private val repository: Repository) : ViewModel() {

    /** COROUTINESCOPE */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** LOADING STATUS */
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun doneDisplayingError() {
        _error.value = null
    }

    /** PREPARE CHAPTER for MODIFICATION */
    private val _chapter = MutableLiveData<Chapter>()
    val chapter: LiveData<Chapter>
        get() = _chapter

    fun prepareChapter(chapter: Chapter) {
        _chapter.value = chapter
    }

    /** SAVING CHAPTER */
    private val _isSavingText = MutableLiveData<Boolean>()
    val isSavingText: LiveData<Boolean>
        get() = _isSavingText

    fun saveChapter() {
        // BLOCK MULTIPLE CLICKING
        if(isSavingText.value == true) return

        _isSavingText.value = true
        _status.value = ApiLoadingStatus.LOADING

        // CHECK if ALL DATA are COMPLETE
        _chapter.value?.let {chapter ->
            coroutineScope.launch {
                val result = repository.postChapter(chapter)
                when(result) {
                    is Result.Success -> {

                        if (result.data) {
                            _status.value = ApiLoadingStatus.ERROR
                        } else {
                            _status.value = ApiLoadingStatus.DONE
                        }
                        _error.value = null
                        _isSavingText.value = null
                    }

                    is Result.Fail -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.error
                        _isSavingText.value = null
                    }

                    is Result.Error -> {
                        _status.value = ApiLoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        _isSavingText.value = null
                    }
                }
            }
        }
    }

    /** NAVIGATE to MODIFICATION PAGE */
    private val _shouldNavigateToModificationPage = MutableLiveData<Boolean>()
    val shouldNavigateToModificationPage: LiveData<Boolean>
        get() = _shouldNavigateToModificationPage

    fun navigateToModificationPage() {
        _shouldNavigateToModificationPage.value = true
    }

    fun doneNavigateToModificationPage() {
        _shouldNavigateToModificationPage.value = null
    }


    /** NAVIGATE back to PREVIOUS PAGE */
    private val _shouldGoBackToPreviousPage = MutableLiveData<Boolean>()
    val shouldGoBackToPreviousPage: LiveData<Boolean>
        get() = _shouldGoBackToPreviousPage

    fun backToPreviousPage() {
        _shouldGoBackToPreviousPage.value = true
    }

    fun doneNavigatingToPreviousPage() {
        _shouldGoBackToPreviousPage.value = null
    }


}