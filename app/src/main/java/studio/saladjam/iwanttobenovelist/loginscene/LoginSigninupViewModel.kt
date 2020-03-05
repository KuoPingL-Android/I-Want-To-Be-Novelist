package studio.saladjam.iwanttobenovelist.loginscene

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util.getString
import studio.saladjam.iwanttobenovelist.repository.Repository
import studio.saladjam.iwanttobenovelist.repository.Result
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus


class LoginSigninupViewModel (private val repository: Repository): ViewModel() {

    private val _status = MutableLiveData<ApiLoadingStatus>()
    val loadingStatus: LiveData<ApiLoadingStatus>
        get() = _status

    private val _dialogInfo = MutableLiveData<Pair<String, ApiLoadingStatus>>()
    val dialogInfo: LiveData<Pair<String, ApiLoadingStatus>>
        get() = _dialogInfo

    fun doneDisplayingDialog() {
        _dialogInfo.value = null
        _status.value = null
        _error.value = null
    }

    val isLoading = MediatorLiveData<Boolean>().apply {
        addSource(loadingStatus){ value = loadingStatus.value == ApiLoadingStatus.LOADING }
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    /**LOGIN WITH FACEBOOK*/
    private val _loginWithFacebook = MutableLiveData<Boolean>()
    val loginWithFacebook: LiveData<Boolean>
        get() = _loginWithFacebook

    lateinit var fbCallBackManager: CallbackManager

    fun loginWithFacebookSelected() {
        _loginWithFacebook.value = true
    }

    fun doneLoggingInWithFacebook() {
        _loginWithFacebook.value = null
        loginFromFb()
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
        _shouldNavigateToNextLoginPage.value = true
    }


    private fun loginFromFb() {
        if (IWBNApplication.isNetworkConnected) {
            _status.value = ApiLoadingStatus.LOADING
            fbCallBackManager = CallbackManager.Factory.create()
            coroutineScope.launch {

                when(val result = repository.loginViaFacebook(fbCallBackManager)) {
                    is Result.Success -> {

                        if (result.data) {
                            doubleCheckUserExistence()
                        } else {
                            // Something is Wrong
                            _status.value = ApiLoadingStatus.ERROR
                        }
                    }

                    is Result.Error -> {
                        _error.value = result.exception.toString()
                        _status.value = ApiLoadingStatus.ERROR
                    }

                    is Result.Fail -> {
                        Logger.i("ERROR = ${result.error}")
                        _status.value = ApiLoadingStatus.ERROR
                    }
                }
            }
        } else {
//            _dialogInfo.value = Pair(getString(R.string.internet_not_connected), APILoadingStatus.ERROR)
        }
    }

    /** LOGIN WITH GOOGLE */
    private val _loginWithGoogle = MutableLiveData<Boolean>()
    val loginWithGoogle: LiveData<Boolean>
        get() = _loginWithGoogle

    fun loginWithGoogleSelected() {
        _loginWithGoogle.value = true
    }

    fun doneLoggingInWithGoogle() {
        _loginWithGoogle.value = null
    }

    var mGoogleSignInClient: GoogleSignInClient? = null

    fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>) {
        when(val result = repository.handleGoogleTask(completedTask)) {
            is Result.Success -> {
                doubleCheckUserExistence()
            }

            is Result.Fail -> {
                _error.value = result.error
            }

            is Result.Error -> {
                Logger.i("handleGoogleTask ERROR : ${result.exception.message}")
            }
        }
    }


    /** DOUBLE CHECK if USER EXISTs before asking for names */
    fun doubleCheckUserExistence() {
        if (IWBNApplication.isNetworkConnected) {
            _dialogInfo.value = Pair("", ApiLoadingStatus.LOADING)
            coroutineScope.launch {
                when(val result = repository.checkIfUserExist(IWBNApplication.user)){
                    is Result.Success -> {

                        if (result.data) {
                            _dialogInfo.value = Pair(getString(R.string.hint_success_login), ApiLoadingStatus.DONE)
                            navigateToHomePage()
                        } else {
                            promptToAskForName()
                            _dialogInfo.value = Pair("", ApiLoadingStatus.DONE)
                        }
                    }

                    is Result.Fail -> {
                        _dialogInfo.value = Pair("", ApiLoadingStatus.ERROR)
                    }

                    is Result.Error -> {
                        _error.value = result.exception.localizedMessage
                        _dialogInfo.value = Pair("", ApiLoadingStatus.ERROR)
                    }
                }
            }
        } else {
            _dialogInfo.value = Pair(getString(R.string.internet_not_connected), ApiLoadingStatus.ERROR)
        }
    }

    /**NAVIGATION*/
    // HOME_PAGE
    private val _shouldNavigateToHomePage = MutableLiveData<Boolean>()
    val shouldNavigateToHomePage: LiveData<Boolean>
        get() = _shouldNavigateToHomePage

    fun navigateToHomePage() {
        _shouldNavigateToHomePage.value = true
    }

    fun doneNavigateToHomePage() {
        _shouldNavigateToHomePage.value = null
    }

    // NAVIGATE to NEXT PAGE
    private val _shouldNavigateToNextLoginPage = MutableLiveData<Boolean>()
    val shouldNavigateToNextLoginPage: LiveData<Boolean>
        get() = _shouldNavigateToNextLoginPage

    fun navigateToNextLoginPage() {
        _shouldNavigateToNextLoginPage.value = true
    }

    fun doneNavigateToNextLoginPage() {
        _shouldNavigateToNextLoginPage.value = null
    }


}