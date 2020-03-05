package studio.saladjam.iwanttobenovelist.editorscene

import android.graphics.Bitmap
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
import studio.saladjam.iwanttobenovelist.repository.dataclass.ImageBlockRecorder
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class EditorMixerViewModel (private val repository: Repository): ViewModel() {

    /** COROUTINE */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** STATUS */
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** SHOW DIALOG for IMAGES */
    private val _shouldAddImage = MutableLiveData<Boolean>()
    val shouldAddImage: LiveData<Boolean>
        get() = _shouldAddImage

    fun addNewImage() {
        if (_shouldAddImage.value == true) return
        _shouldAddImage.value = true
    }

    fun doneAddingImage() {
        _shouldAddImage.value = null
    }

    /** DELETE IMAGE TRIGGER */
    private val _imageDeleteEnabled = MutableLiveData<Boolean>()
    val imageDeleteEnabled: LiveData<Boolean>
        get() = _imageDeleteEnabled
    private var hasImageDeletionEnabled = false

    fun triggerImageDeletion() {
        _imageDeleteEnabled.value = (!hasImageDeletionEnabled)
        hasImageDeletionEnabled = !hasImageDeletionEnabled
    }

    fun doneTriggeringImageDeletion() {
        _imageDeleteEnabled.value = null
    }

    /** EDIT TEXT */
    private val _shouldAddEditText = MutableLiveData<Boolean>()
    val shouldAddEditText: LiveData<Boolean>
        get() = _shouldAddEditText

    fun addEditText() {
        _shouldAddEditText.value = true
    }

    fun doneAddingEditText() {
        _shouldAddEditText.value = null
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

    /** SAVING CHAPTER */
    private val _isSavingText = MutableLiveData<Boolean>()
    val isSavingText: LiveData<Boolean>
        get() = _isSavingText

    private val _shouldSaveChapter = MutableLiveData<Boolean>()
    val shouldSaveChapter: LiveData<Boolean>
        get() = _shouldSaveChapter

    fun saveChapter() {
        _shouldSaveChapter.value = true
    }

    fun doneSaveChapter() {
        _shouldSaveChapter.value = null
    }
    val test = MutableLiveData<Boolean>()

    fun saveChapterDetails(chapter: Chapter,
                           bitmapsMap: Map<String, Bitmap>,
                           coordinators: List<ImageBlockRecorder>) {

        _status.value = ApiLoadingStatus.LOADING

        coroutineScope.launch {

            when(val result = repository.postChapterWithDetails(chapter, bitmapsMap, coordinators)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = ApiLoadingStatus.DONE
                    _isSavingText.value = null
                }

                is Result.Error -> {
                    _error.value = result.exception.localizedMessage
                    _status.value = ApiLoadingStatus.ERROR
                    _isSavingText.value = null
                }

                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = ApiLoadingStatus.ERROR
                    _isSavingText.value = null
                }
            }
        }

    }



}