package studio.saladjam.iwanttobenovelist.loginscene

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginInterestBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.factories.callbackfactories.CallbackFactory
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter

class LoginInterestFragment(private val completeHandler: LoginPagesCompleteHandler)
    : LoginBaseSubFragment(completeHandler) {
    private lateinit var binding: FragmentLoginInterestBinding

    private val viewModel by viewModels<LoginInterestViewModel> { getVMFactory() }

    // Current Span Count = 3
    private val spanCount = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginInterestBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerInterestCategories.adapter = LoginInterestRecyclerViewAdapter(viewModel)

        binding.recyclerInterestCategories.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val index = binding.recyclerInterestCategories.getChildLayoutPosition(view)

                outRect.top = 5.toPx()
                outRect.bottom = 5.toPx()
                outRect.left = 5.toPx()
                outRect.right = 5.toPx()

                when(index%spanCount) {
                    0 -> {
                        outRect.left = 0.toPx()
                        if (index == 0) {
                            outRect.top = 10.toPx()
                        }
                    }

                    1 -> {
                        if (index == 1) {
                            outRect.top = 10.toPx()
                        }
                    }

                    else -> {
                        outRect.right = 0.toPx()
                        if (index == 2) {
                            outRect.top = 10.toPx()
                        }
                    }
                }
            }
        })

        viewModel.shouldNavigateToHome.observe(this, Observer {
            it?.let {
//                findNavController().navigate(LoginFragmentDirections.actionGlobalHomeFragment())
                val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
                mainViewModel.navigateToHomePage()
                viewModel.doneNavigateToHome()
            }
        })

        binding.viewModel = viewModel

        return binding.root
    }
}