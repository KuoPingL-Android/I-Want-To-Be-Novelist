package studio.saladjam.iwanttobenovelist.editorscene

import android.media.Image
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository

class EditorViewModel (private val repository: Repository): ViewModel() {
    /** IMAGE */
    private val _shouldAddImage = MutableLiveData<Boolean>()
    val shouldAddImage: LiveData<Boolean>
        get() = _shouldAddImage

    private val _newImage = MutableLiveData<Image>()
    val newImage: LiveData<Image>
        get() = _newImage

    fun addNewImage() {
        _shouldAddImage.value = true
    }

    fun doneAddingImage() {
        _shouldAddImage.value = null
    }

    fun selectImage(image: Image) {
        _newImage.value = image
    }

    /** EDIT TEXT */
    private val _shouldAddEditText = MutableLiveData<Boolean>()
    val shouldAddEditText: LiveData<Boolean>
        get() = _shouldAddEditText

//    private val _newEditText = MutableLiveData<EditText>()
//    val newEditText: LiveData<EditText>
//        get() = _newEditText

    fun addEditText() {
        _shouldAddEditText.value = true
    }

    fun doneAddingEditText() {
        _shouldAddEditText.value = null
    }


}