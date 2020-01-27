package studio.saladjam.iwanttobenovelist.loginscene

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

class LoginInterestViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    private val selectedCategory = mutableListOf<String>()

    private val _shouldNavigateToHome = MutableLiveData<Boolean>()
    val shouldNavigateToHome: LiveData<Boolean>
        get() = _shouldNavigateToHome

    fun doneNavigateToHome(){
        _shouldNavigateToHome.value = null
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        coroutineScope.launch {
            val result = repository.getCategory()

            when (result) {
                is Result.Success -> {
                    val list = result.data
                    Logger.i("LIST = ${list}")
                    withContext(Dispatchers.Main) {
                        _categories.value = list
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
        coroutineScope.launch {
            val result = repository.loginUser(user)
            when(result) {
                is Result.Success -> {
                    Logger.i("createUser RESULT : ${result.data}")
                    _shouldNavigateToHome.value = true
                }

                is Result.Error -> {
                    Logger.i("createUser ERROR : ${result.exception}")
                }

                is Result.Fail -> {
                    Logger.i("createUser FAILED : ${result.error}")
                }
            }
        }
    }

}