package studio.saladjam.iwanttobenovelist.loginscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.Repository

class LoginRoleViewModel (private val repository: Repository): ViewModel() {

    private val _roleDescription = MutableLiveData<String>()
    val roleDescription: LiveData<String>
        get() = _roleDescription

    fun selectedWriter() {
        _roleDescription.value = IWBNApplication.context.getString(R.string.hint_writer_description)
    }

    fun selectedReviewer() {
        _roleDescription.value = IWBNApplication.context.getString(R.string.hint_reviewer_description)
    }


    /** ASK FOR NAME */
    private val _shouldAskForName = MutableLiveData<Boolean>()
    val shouldAskForName: LiveData<Boolean>
        get() = _shouldAskForName

    fun promptToAskForName() {
        _shouldAskForName.value = true
    }

    fun doneAskingForName() {
        _shouldAskForName.value = null
    }

    fun updateUserName(name: String) {
        IWBNApplication.user.name = name
        _shouldGoToNextPage.value = true
    }

    /** GO TO NEXT PAGE */
    private val _shouldGoToNextPage = MutableLiveData<Boolean>()
    val shouldToGoNextPage: LiveData<Boolean>
        get() = _shouldGoToNextPage

    fun doneGoingToNextPage() {
        _shouldGoToNextPage.value = null
    }



}