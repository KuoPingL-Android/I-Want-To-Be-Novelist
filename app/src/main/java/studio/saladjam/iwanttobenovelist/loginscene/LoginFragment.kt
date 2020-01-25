package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginBinding
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginFragmentViewPagerAdapter
import studio.saladjam.iwanttobenovelist.loginscene.pagesenum.LoginPages

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var adapter: LoginFragmentViewPagerAdapter

    /**LOGIN PAGES and COMPLETE HANDLER*/
    private val pages = listOf(LoginPages.SIGNINUP, LoginPages.SELECT_ROLE, LoginPages.SELECT_INTERESTS)
    private val completeHandler: LoginPagesCompleteHandler = {isCompleted ->
        if (isCompleted) {
            // Check if this is the last item
            val currentItem = binding.viewpagerLogin.currentItem
            val maxIndex = adapter.itemCount - 1

            if (currentItem < maxIndex) {
                binding.viewpagerLogin.setCurrentItem(currentItem + 1, true)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        adapter = LoginFragmentViewPagerAdapter(this, pages, completeHandler)

        binding.viewpagerLogin.adapter = adapter

//        binding.viewpagerLogin.isUserInputEnabled = false

        return binding.root
    }
}