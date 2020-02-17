package studio.saladjam.iwanttobenovelist.profilescene

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
import com.google.android.material.tabs.TabLayoutMediator
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileFragmentStateAdapter

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.viewpagerProfile.adapter = ProfileFragmentStateAdapter(this, viewModel)

        val fragments = listOf(ProfileBookReadingFragment(viewModel), ProfileWorkFragment(viewModel))
        (binding.viewpagerProfile.adapter as? ProfileFragmentStateAdapter)?.setFragments(fragments)

        setupTabLayout()

        viewModel.fetchUserWork()

        return binding.root
    }

    private fun setupTabLayout() {
        binding.tablayoutProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(Color.parseColor("#ee888888"))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(Color.parseColor("#000000"))
            }

        })

        binding.tablayoutProfile.isTabIndicatorFullWidth = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(binding.tablayoutProfile, binding.viewpagerProfile) {tab, position ->
            tab.setTabLabelVisibility(TAB_LABEL_VISIBILITY_UNLABELED)
            when(position) {
                0 -> {
                    tab.icon = (IWBNApplication.context.getDrawable(R.drawable.bookmark_icon))
                    tab.icon?.setTint(Color.parseColor("#000000"))
                }

                1 ->  {
                    tab.icon = (IWBNApplication.context.getDrawable(R.drawable.pen_icon))
                    tab.icon?.setTint(Color.parseColor("#ee888888"))
                }
            }


        }.attach()
    }
}