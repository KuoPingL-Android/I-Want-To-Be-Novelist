package studio.saladjam.iwanttobenovelist.profilescene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

class ProfileViewModel (private val repository: Repository): ViewModel() {

    private val _user = MutableLiveData<User>().apply {
        value = IWBNApplication.user
    }
    val user: LiveData<User>
        get() = _user

    private val _shouldCreateNewBook = MutableLiveData<Boolean>()
    val shouldCreateNewBook: LiveData<Boolean>
        get() = _shouldCreateNewBook

    fun createNewBook() {
        _shouldCreateNewBook.value = true
    }

    fun doneNavigateToNewBook() {
        _shouldCreateNewBook.value = null
    }


}