package studio.saladjam.iwanttobenovelist.categoryscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result

class CategoryViewModel(private val repository: Repository): ViewModel() {

    /** REPOSITORY */
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    /** CATEGORY */
    private val _categories = MutableLiveData<List<String>>()
    val category: MutableLiveData<List<String>>
        get() = _categories

    private fun fetchCategories() {

        coroutineScope.launch {
            val result = repository.getCategory()
            when (result) {
                is Result.Success -> {
                    val list = result.data
                    Logger.i("LIST = ${list}")
                    withContext(Dispatchers.Main) {
                        _categories.value = list.getListFor()
                    }
                }

                is Result.Error -> {
                    _error.value = result.exception.toString()
                }

                is Result.Fail -> {
                    _error.value = result.error
                }
            }
        }
    }

    /** SELECTED CATEGORY & LANGUAGE */
    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private val _selectedLanguage = MutableLiveData<String>()
    val selectedLanguage: LiveData<String>
        get() = _selectedLanguage

    private fun fetchBooks() {

    }

    fun fetchData() {
        coroutineScope.launch {

        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}