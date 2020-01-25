package studio.saladjam.iwanttobenovelist.loginscene.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import studio.saladjam.iwanttobenovelist.loginscene.LoginPagesCompleteHandler
import studio.saladjam.iwanttobenovelist.loginscene.pagesenum.LoginPages

class LoginFragmentViewPagerAdapter(fragment: Fragment,
                                    val pages: List<LoginPages>,
                                    val pagesCompleteHandler: LoginPagesCompleteHandler)
    :FragmentStateAdapter(fragment){



    override fun getItemCount(): Int {
        return pages.count()
    }

    override fun createFragment(position: Int): Fragment {
        val page = pages[position]
        return LoginPagesCreator.createFragmentFor(page, pagesCompleteHandler)
    }
}