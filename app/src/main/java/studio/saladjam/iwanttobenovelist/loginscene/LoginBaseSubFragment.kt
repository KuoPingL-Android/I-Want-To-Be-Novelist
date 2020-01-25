package studio.saladjam.iwanttobenovelist.loginscene

import androidx.fragment.app.Fragment

typealias LoginPagesCompleteHandler = (isDone: Boolean) -> Unit

open class LoginBaseSubFragment(private val completeHandler: LoginPagesCompleteHandler) : Fragment()