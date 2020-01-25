package studio.saladjam.iwanttobenovelist.loginscene.adapter

import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.loginscene.LoginInterestFragment
import studio.saladjam.iwanttobenovelist.loginscene.LoginPagesCompleteHandler
import studio.saladjam.iwanttobenovelist.loginscene.LoginRoleFragment
import studio.saladjam.iwanttobenovelist.loginscene.LoginSigninupFragment
import studio.saladjam.iwanttobenovelist.loginscene.pagesenum.LoginPages

class LoginPagesCreator {

    companion object {
        fun createFragmentFor(page: LoginPages, completeHandler: LoginPagesCompleteHandler): Fragment {
            return when(page) {
                LoginPages.SIGNINUP -> {
                    LoginSigninupFragment(completeHandler)
                }

                LoginPages.SELECT_ROLE -> {
                    LoginRoleFragment(completeHandler)
                }

                LoginPages.SELECT_INTERESTS -> {
                    LoginInterestFragment(completeHandler)
                }
            }
        }
    }
}