package studio.saladjam.iwanttobenovelist.loginscene.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class LoginInterestViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<Genre>>()
    val categories: LiveData<List<Genre>>
        get() = _categories

    private val selectedCategory = mutableListOf<String>()

    private val _shouldNavigateToHome = MutableLiveData<Boolean>()
    val shouldNavigateToHome: LiveData<Boolean>
        get() = _shouldNavigateToHome

    fun doneNavigateToHome(){
        _shouldNavigateToHome.value = null
    }

    private val _status = MutableLiveData<ApiLoadingStatus>()
    val status: LiveData<ApiLoadingStatus>
        get() = _status

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /** DIALOG INFO */
    private val _dialogInfo = MutableLiveData<Pair<String, ApiLoadingStatus>>()
    val dialogInfo: LiveData<Pair<String, ApiLoadingStatus>>
        get() = _dialogInfo

    fun doneShowingDialog() {
        _dialogInfo.value = null
    }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        coroutineScope.launch {
            when (val result = repository.getCategory()) {
                is Result.Success -> {
                    val list = result.data
                    Logger.i("LIST = ${list}")
                    withContext(Dispatchers.Main) {
                        _categories.value = list.genres
                    }
                }

                is Result.Error -> {

                }

                is Result.Fail -> {

                }
            }
        }
    }

    /** INTERACTION with ITEM */
    fun categorySelected(view: View) {
        val category = view.tag as String
        if (selectedCategory.contains(category)) {
            selectedCategory.remove(category)
            view.isSelected = false
        } else {
            if (selectedCategory.count() == IWBNApplication.context.getString(R.string.max_number_interest).toInt()) {
                _error.value = IWBNApplication.context.getString(R.string.hint_too_many_interest)
            } else {
                selectedCategory.add(category)
                view.isSelected = true
            }
        }
    }

    /** CREATE USER */
    fun createUser() {
        val user = IWBNApplication.user
        user.preferredCategories = selectedCategory

        _status.value = ApiLoadingStatus.LOADING
        _dialogInfo.value = Pair("", _status.value!!)

        coroutineScope.launch {

            when(val result = repository.loginUser(user)) {
                is Result.Success -> {
                    Logger.i("createUser RESULT : ${result.data}")
                    _status.value = ApiLoadingStatus.DONE
                    _error.value = null
                    _dialogInfo.value = Pair(IWBNApplication.context.getString(R.string.button_complete), _status.value!!)
                    _shouldNavigateToHome.value = true
                }

                is Result.Error -> {
                    Logger.i("createUser ERROR : ${result.exception}")
                    _error.value = result.exception.localizedMessage
                    _status.value = ApiLoadingStatus.ERROR
                    _dialogInfo.value = Pair("", _status.value!!)
                }

                is Result.Fail -> {
                    Logger.i("createUser FAILED : ${result.error}")
                    _error.value = result.error
                    _status.value = ApiLoadingStatus.ERROR
                    _dialogInfo.value = Pair("", _status.value!!)
                }
            }
        }
    }

}