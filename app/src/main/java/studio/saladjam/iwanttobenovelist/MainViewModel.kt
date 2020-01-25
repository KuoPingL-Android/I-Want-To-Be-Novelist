package studio.saladjam.iwanttobenovelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.saladjam.iwanttobenovelist.repository.Repository

class MainViewModel(private val repository: Repository): ViewModel() {
    /***
     * LIVE DATAs and Corresponding ACTIONS
     */

    /** NAVIGATE to LOGIN PAGE*/
    private val _shouldNavigateToLoginPage = MutableLiveData<Boolean>()
    val shouldNavigateToLoginPage: LiveData<Boolean>
        get() = _shouldNavigateToLoginPage

    private fun navigateToLoginPage() {
        _shouldNavigateToLoginPage.value = true
    }

    fun navigatedToLoginPage() {
        _shouldNavigateToLoginPage.value = null
    }

    /** NAVIGATE to HOME PAGE */
    private val _shouldNavigateToHomePage = MutableLiveData<Boolean>()
    val shouldNavigateToHomePage: LiveData<Boolean>
        get() = _shouldNavigateToHomePage

    fun navigateToHomePage() {
        _shouldNavigateToHomePage.value = true
    }

    fun navigatedToHomePage() {
        _shouldNavigateToHomePage.value = null
    }

    /**CHECK if USER is NEW USER*/
    fun prepareUserData() {
        if (UserManager.userToken == null) {
            // redirect user to Login Page
            navigateToLoginPage()
        } else {
            navigateToHomePage()
        }
    }


}