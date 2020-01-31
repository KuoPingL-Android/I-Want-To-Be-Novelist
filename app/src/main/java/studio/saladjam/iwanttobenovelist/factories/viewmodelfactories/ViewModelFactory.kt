package studio.saladjam.iwanttobenovelist.factories.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.bookscene.BookViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.launchscene.LaunchViewModel
import studio.saladjam.iwanttobenovelist.loginscene.*
import studio.saladjam.iwanttobenovelist.repository.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {

                isAssignableFrom(LaunchViewModel::class.java)
                                    -> LaunchViewModel(repository)

                isAssignableFrom(LoginSigninupViewModel::class.java)
                                    -> LoginSigninupViewModel(repository)

                isAssignableFrom(LoginRoleViewModel::class.java)
                                    -> LoginRoleViewModel(repository)

                isAssignableFrom(LoginSelectNameViewModel::class.java)
                                    -> LoginSelectNameViewModel(repository)

                isAssignableFrom(LoginInterestViewModel::class.java)
                                    -> LoginInterestViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java)
                                    -> HomeViewModel(repository)

                isAssignableFrom(BookViewModel::class.java)
                                    -> BookViewModel(repository)

                else -> {throw IllegalArgumentException("${modelClass} is not supported")}
            }
        } as T
    }
}