package studio.saladjam.iwanttobenovelist.editorscene

import android.graphics.Bitmap
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.ImageBlockRecorder
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class EditorViewModel (private val repository: Repository) : ViewModel() {

    /** COROUTINESCOPE */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** LOADING STATUS */
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** PREPARE CHAPTER for MODIFICATION */
    private val _chapter = MutableLiveData<Chapter>()
    val chapter: LiveData<Chapter>
        get() = _chapter

    /** CALLED when ENTERING EDITOR FRAGMENT */
    fun prepareChapter(chapter: Chapter) {
        _chapter.value = chapter
        if (chapter.chapterID.isEmpty()) {
            _isCurrentlyEditText.value = true
        } else {
            // FETCH CHAPTER DETAIL
            getChapterDetails()
        }
    }

    /** SAVING CHAPTER */
    private val _isSaving = MutableLiveData<Boolean>()
    val isSaving: LiveData<Boolean>
        get() = _isSaving

    /** NAVIGATE to MODIFICATION PAGE */
    private val _isCurrentlyEditText = MutableLiveData<Boolean>()
    val isCurrentlyEditText: LiveData<Boolean>
        get() = _isCurrentlyEditText


    fun changeEditingMode() {
        _isCurrentlyEditText.value = !(_isCurrentlyEditText.value ?: false)
    }

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

    /** LOAIDNG DIALOG */
    private val _dialogInfo = MutableLiveData<Pair<String, APILoadingStatus>>()
    val dialogInfo: LiveData<Pair<String, APILoadingStatus>>
        get() = _dialogInfo

    fun doneDisplayingDialog() {
        _dialogInfo.value = null
    }

    /** SAVING CHAPTER */
    private val _shouldSaveChapter = MutableLiveData<Boolean>()
    val shouldSaveChapter: LiveData<Boolean>
        get() = _shouldSaveChapter

    fun saveChapter() {
        _shouldSaveChapter.value = true
    }

    fun doneSaveChapter() {
        _shouldSaveChapter.value = null
    }

    fun saveChapterDetails(chapter: Chapter,
                           bitmapsMap: Map<String, Bitmap>,
                           coordinators: List<ImageBlockRecorder>) {

        if(isSaving.value == true) return

        _isSaving.value = true

        _status.value = APILoadingStatus.LOADING

        _dialogInfo.value = Pair("", _status.value!!)

        coroutineScope.launch {

            when(val result = repository.postChapterWithDetails(chapter, bitmapsMap, coordinators)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = APILoadingStatus.DONE
                    _isSaving.value = null
                    _dialogInfo.value = Pair(IWBNApplication.context.resources.getString(R.string.editor_save), _status.value!!)
                }

                is Result.Error -> {
                    _error.value = result.exception.localizedMessage
                    _status.value = APILoadingStatus.ERROR
                    _isSaving.value = null
                    _dialogInfo.value = Pair(IWBNApplication.context.resources.getString(R.string.editor_error_save), _status.value!!)
                }

                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = APILoadingStatus.ERROR
                    _isSaving.value = null
                    _dialogInfo.value = Pair(IWBNApplication.context.resources.getString(R.string.editor_error_save), _status.value!!)
                }
            }
        }

    }

    private val _currentChapterBlock = MutableLiveData<List<ImageBlockRecorder>>()
    val currentChapterBlock: LiveData<List<ImageBlockRecorder>>
        get() = _currentChapterBlock

    fun getChapterDetails() {

        chapter.value?.let {chapter ->
            coroutineScope.launch {

                _status.value = APILoadingStatus.LOADING

                _dialogInfo.value = Pair("", _status.value!!)

                when(val result = repository.getChapterWithDetails(chapter.chapterIndex, chapter.bookID)) {
                    is Result.Success -> {
                        _currentChapterBlock.value = result.data.second
                        _status.value = APILoadingStatus.DONE
                        _dialogInfo.value = Pair(IWBNApplication.context.resources.getString(R.string.editor_done), _status.value!!)
                    }

                    is Result.Fail -> {
                        _error.value = result.error
                        _currentChapterBlock.value = null
                        _status.value = APILoadingStatus.ERROR
                        _dialogInfo.value = Pair("", _status.value!!)
                    }

                    is Result.Error -> {
                        _error.value = result.exception.localizedMessage
                        _currentChapterBlock.value = null
                        _status.value = APILoadingStatus.ERROR
                        _dialogInfo.value = Pair("", _status.value!!)
                    }
                }
            }
        }
    }

    /** NAVIGATE back to PREVIOUS PAGE */
    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean>
        get() = _isDone

    fun pressIsDone() {
        _isDone.value = true
    }

    fun donePressingIsDone() {
        _isDone.value = null
    }
}