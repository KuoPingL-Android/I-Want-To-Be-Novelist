package studio.saladjam.iwanttobenovelist.homescene

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeRecyclerAdpaterV1

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeV1Binding
    private val viewModel by viewModels<HomeViewModel> { getVMFactory() }
    private val workInProgressViewModel by viewModels<HomeWorkInProgressViewModel> { getVMFactory() }
    private var mainViewModel: MainViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeV1Binding.inflate(inflater)
        binding.lifecycleOwner = this

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.selectedBookToRead(it)
                viewModel.doneSelectingBook()
            }
        })

        viewModel.areDataRead.observe(viewLifecycleOwner, Observer {
            it?.let {isReady ->
                if(isReady) {
                    viewModel.prepareFinalList()
                }
            }
        })

        binding.recyclerHomeMain.adapter = HomeRecyclerAdpaterV1(viewModel, workInProgressViewModel)

        binding.recyclerHomeMain.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {

                val space = (parent.height * 0.1).toInt()

                outRect.bottom = 15.toPx()

                when(parent.getChildLayoutPosition(view)) {
                    0 -> outRect.top = 15.toPx()
                    else -> outRect.top = 15.toPx()
                }
            }
        })

        viewModel.shouldNavigateToMyWork.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.navigateToProfilePage(MainViewModel.PROFILETABS.WORKS)
                viewModel.doneNavigateToMyWork()
            }
        })

        viewModel.shouldNavigateToMyFollow.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.navigateToProfilePage(MainViewModel.PROFILETABS.DEFAULT)
                viewModel.doneNavigateToMyFollow()
            }
        })

        viewModel.shouldNavigateToPopular.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.navigateToSearchPopular()
                viewModel.doneNavigateToPopular()
            }
        })

        viewModel.shouldNavigateToRecommend.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.navigateToSearchRecommend()
                viewModel.doneNavigateToRecommend()
            }
        })

        binding.viewModel = viewModel

        viewModel.fetchDatas()
        return binding.root
    }
}