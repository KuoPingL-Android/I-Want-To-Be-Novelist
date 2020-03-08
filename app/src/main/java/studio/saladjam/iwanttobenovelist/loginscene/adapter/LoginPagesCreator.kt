package studio.saladjam.iwanttobenovelist.loginscene.adapter

import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.loginscene.LoginInterestFragment
import studio.saladjam.iwanttobenovelist.loginscene.LoginPagesCompleteHandler
import studio.saladjam.iwanttobenovelist.loginscene.LoginSigninupFragment
import studio.saladjam.iwanttobenovelist.loginscene.pagesenum.LoginPages
import java.lang.IllegalArgumentException

class LoginPagesCreator {

    companion object {
        fun createFragmentFor(page: LoginPages, completeHandler: LoginPagesCompleteHandler): Fragment {
            return when(page) {
                LoginPages.SIGNINUP -> {
                    LoginSigninupFragment(completeHandler)
                }

                LoginPages.SELECT_INTERESTS -> {
                    LoginInterestFragment(completeHandler)
                }

                else -> {
                    throw IllegalArgumentException(
                        IWBNApplication.instance
                            .getString(R.string.exception_page_not_found, this::class.java))
                }
            }
        }
    }
}