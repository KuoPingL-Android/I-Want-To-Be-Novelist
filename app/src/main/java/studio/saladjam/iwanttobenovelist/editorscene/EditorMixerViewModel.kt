package studio.saladjam.iwanttobenovelist.editorscene

import android.media.Image
import android.widget.EditText
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
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class EditorMixerViewModel (private val repository: Repository): ViewModel() {

    /** COROUTINE */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** STATUS */
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** SHOW DIALOG for IMAGES */
    private val _shouldAddImage = MutableLiveData<Boolean>()
    val shouldAddImage: LiveData<Boolean>
        get() = _shouldAddImage

    private val _newImage = MutableLiveData<Image>()
    val newImage: LiveData<Image>
        get() = _newImage

    fun addNewImage() {
        if (_shouldAddImage.value == true) return
        _shouldAddImage.value = true
    }

    fun doneAddingImage() {
        _shouldAddImage.value = null
    }

    fun selectImage(image: Image) {
        _newImage.value = image
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

    fun saveChapter() {
        // BLOCK MULTIPLE CLICKING
        if(isSavingText.value == true) return

        _isSavingText.value = true
        _status.value = APILoadingStatus.LOADING

        // CHECK if ALL DATA are COMPLETE
        _chapter.value?.let {chapter ->
            coroutineScope.launch {
                val result = repository.postChapter(chapter)
                when(result) {
                    is Result.Success -> {

                        if (result.data) {
                            _status.value = APILoadingStatus.ERROR
                        } else {
                            _status.value = APILoadingStatus.DONE
                        }
                        _error.value = null
                        _isSavingText.value = null
                    }

                    is Result.Fail -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.error
                        _isSavingText.value = null
                    }

                    is Result.Error -> {
                        _status.value = APILoadingStatus.ERROR
                        _error.value = result.exception.localizedMessage
                        _isSavingText.value = null
                    }
                }
            }
        }
    }


    private val _chapter = MutableLiveData<Chapter>()
    val chapter: LiveData<Chapter>
        get() = _chapter

    fun setChapter(chapter: Chapter) {
        _chapter.value = chapter


    }


}