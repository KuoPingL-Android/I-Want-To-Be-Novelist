package studio.saladjam.iwanttobenovelist.profilescene

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_LABELED
import com.google.android.material.tabs.TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileFragmentStateAdapter

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> { getVMFactory() }

    private var initialTabNumber = -1



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
                tab?.tabLabelVisibility = (TAB_LABEL_VISIBILITY_LABELED)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(Color.parseColor("#000000"))
                tab?.tabLabelVisibility = (TAB_LABEL_VISIBILITY_UNLABELED)
            }

        })

        binding.tablayoutProfile.setSelectedTabIndicatorColor(resources.getColor(android.R.color.black))

        binding.tablayoutProfile.isTabIndicatorFullWidth = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(binding.tablayoutProfile, binding.viewpagerProfile) {tab, position ->
            when(position) {
                0 -> {
                    tab.icon = (IWBNApplication.context.getDrawable(R.drawable.bookmark_icon))
                    tab.icon?.setTint(Color.parseColor("#000000"))
                    tab.text = "追蹤書籍"
                }

                1 ->  {
                    tab.icon = (IWBNApplication.context.getDrawable(R.drawable.pen_icon))
                    tab.icon?.setTint(Color.parseColor("#ee888888"))
                    tab.text = "個人作品"
                }
            }
        }.attach()

        val tag = requireArguments().get("tab") as Int

        if (initialTabNumber != tag) {
            initialTabNumber = tag
            CoroutineScope(Dispatchers.Main).launch {
                binding.viewpagerProfile.currentItem = initialTabNumber
            }
        }

    }
}