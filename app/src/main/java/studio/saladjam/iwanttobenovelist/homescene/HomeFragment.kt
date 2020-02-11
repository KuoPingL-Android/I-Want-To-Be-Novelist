package studio.saladjam.iwanttobenovelist.homescene

import android.graphics.Rect
import android.icu.lang.UCharacter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.bind
import studio.saladjam.iwanttobenovelist.databinding.FragmentHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeBookRecyclerAdapter
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeGeneralViewHolder
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeRecyclerAdpaterV1
import studio.saladjam.iwanttobenovelist.homescene.adapters.RecommendRecyclerAdpater
import studio.saladjam.iwanttobenovelist.repository.dataclass.Roles

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeV1Binding
    private val viewModel by viewModels<HomeViewModel> { getVMFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeV1Binding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.selectedBook.observe(this, Observer {
            it?.let {
                Logger.i("GO TO BOOK PAGE")
                viewModel.doneSelectingBook()
            }
        })

        viewModel.areDataRead.observe(this, Observer {
            it?.let {isReady ->
                if(isReady) {
                    viewModel.prepareFinalList()
                }
            }
        })

        binding.recyclerHomeMain.adapter = HomeRecyclerAdpaterV1(viewModel)

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
                    0 -> outRect.top = space
                    else -> outRect.top = 15.toPx()
                }
            }
        })

        binding.viewModel = viewModel

        viewModel.fetchDatas()
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val homeLocation = IntArray(2)
//        binding.imageHomeV1Logo.getLocationOnScreen(homeLocation)
//
//        val searchBarLocation = IntArray(2)
//        binding.searchHomeV1.getLocationOnScreen(searchBarLocation)
//
//        binding.searchHomeV1.maxWidth = searchBarLocation.first() - (homeLocation.first() + binding.imageHomeV1Logo.width + 16.toPx())
//
//    }
}