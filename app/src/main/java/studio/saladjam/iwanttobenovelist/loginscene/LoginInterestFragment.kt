package studio.saladjam.iwanttobenovelist.loginscene

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginInterestBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.loginscene.adapter.LoginInterestRecyclerViewAdapter
import studio.saladjam.iwanttobenovelist.loginscene.viewmodel.LoginInterestViewModel

class LoginInterestFragment(private val completeHandler: LoginPagesCompleteHandler)
    : LoginBaseSubFragment(completeHandler) {

    companion object {
        private val ITEM_DECORATOR_NORMAL_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_SMALL
        private val ITEM_DECORATOR_ENDS_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
    }

    private lateinit var binding: FragmentLoginInterestBinding

    private val viewModel by viewModels<LoginInterestViewModel> { getVMFactory() }
    private val mainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

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

                outRect.top     = ITEM_DECORATOR_NORMAL_MARGIN
                outRect.bottom  = ITEM_DECORATOR_NORMAL_MARGIN
                outRect.left    = ITEM_DECORATOR_NORMAL_MARGIN
                outRect.right   = ITEM_DECORATOR_NORMAL_MARGIN

                when(index%spanCount) {
                    0 -> {
                        outRect.left = 0.toPx()
                        if (index == 0) {
                            outRect.top = ITEM_DECORATOR_ENDS_MARGIN
                        }
                    }

                    1 -> {
                        if (index == 1) {
                            outRect.top = ITEM_DECORATOR_ENDS_MARGIN
                        }
                    }

                    else -> {
                        outRect.right = 0.toPx()
                        if (index == 2) {
                            outRect.top = ITEM_DECORATOR_ENDS_MARGIN
                        }
                    }
                }
            }
        })

        viewModel.shouldNavigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel.navigateToHomePage()
                viewModel.doneNavigateToHome()
            }
        })

        viewModel.dialogInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel.displayLoadingDialog(it.first, it.second)
                viewModel.doneShowingDialog()
            }
        })

        binding.viewModel = viewModel

        return binding.root
    }
}