package studio.saladjam.iwanttobenovelist.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class LoadingDialogViewModel : ViewModel() {
    private val _status = MutableLiveData<APILoadingStatus>()
    val status: LiveData<APILoadingStatus>
        get() = _status

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun setMessage(message: String, forStatus: APILoadingStatus) {
        _message.value = message
        _status.value = forStatus
    }
}