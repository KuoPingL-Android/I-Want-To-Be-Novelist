package studio.saladjam.iwanttobenovelist.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class LoadingDialogViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun setMessage(message: String, forStatus: ApiLoadingStatus) {
        _message.value = message
        _status.value = forStatus
    }
}