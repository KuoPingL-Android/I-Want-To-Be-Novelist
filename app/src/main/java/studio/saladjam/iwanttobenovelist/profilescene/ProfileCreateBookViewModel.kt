package studio.saladjam.iwanttobenovelist.profilescene

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class ProfileCreateBookViewModel (private val repository: Repository): ViewModel() {

    private val job = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    fun donePerformingStatu() {
        _status.value = null
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** FUNCTION to FETCH IMAGES via SELECTION */
    private val _shouldShowImageSelection = MutableLiveData<Boolean>()
    val shouldShowImageSelection: LiveData<Boolean>
        get() = _shouldShowImageSelection

    fun presentImageSelectionDialog() {
        _shouldShowImageSelection.value = true
    }

    fun doneImageSelectionDialog() {
        _shouldShowImageSelection.value = null
    }

    /** FUNCTION to FETCH IMAGE via CAMERA or FOLDER */
    private val _shouldSelectFromCamera = MutableLiveData<Boolean>()
    val shouldSelectFromCamera: LiveData<Boolean>
        get() = _shouldSelectFromCamera

    fun displayCamera() {
        _shouldSelectFromCamera.value = true
    }

    fun doneDisplayingCamera() {
        _shouldSelectFromCamera.value = null
    }

    private val _shouldSelectFromFolder = MutableLiveData<Boolean>()
    val shouldSelectFromFolder: LiveData<Boolean>
        get() = _shouldSelectFromFolder

    fun displayImageSelector() {
        _shouldSelectFromFolder.value = true
    }

    fun doneDisplayingImageSelector() {
        _shouldSelectFromFolder.value = null
    }

    /** IMAGE */
    private val _selectedImage = MutableLiveData<Bitmap>()
    val selectedImage: LiveData<Bitmap>
        get() = _selectedImage

    fun setImage(bitmap: Bitmap?) {
        _selectedImage.value = bitmap
    }

    /** TITLE */
    val title = MutableLiveData<String>()

    /** CHECK if IMAGE and TITLE is FILLED */

    val isDataPrepared = MediatorLiveData<Boolean>() .apply{
        addSource(selectedImage){checkIfDataIsReady()}
        addSource(title) {checkIfDataIsReady()}
    }

    private fun checkIfDataIsReady() {
        if(selectedImage.value == null || title.value == null || title.value?.isEmpty() == true) {
            _error.value = "MISSING DATA"
            isDataPrepared.value = false
            return
        }

        isDataPrepared.value = true
    }

    private val _createdBook = MutableLiveData<Book>()
    val createdBook: LiveData<Book>
        get() = _createdBook

    fun createBook() {
        _status.value = ApiLoadingStatus.LOADING
        coroutineScope.launch {
            val result = repository.createBook(title.value!!, _selectedImage.value!!)
            when(result) {
                is Result.Success -> {
                    _error.value = null
                    _createdBook.value = result.data
                    _status.value = ApiLoadingStatus.DONE
                }

                is Result.Fail -> {
                    _createdBook.value = null
                    _error.value = result.error
                    _status.value = ApiLoadingStatus.ERROR
                }

                is Result.Error -> {
                    _createdBook.value = null
                    _error.value = result.exception.message
                    _status.value = ApiLoadingStatus.ERROR
                }
            }
        }
    }

}