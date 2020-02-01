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
import studio.saladjam.iwanttobenovelist.databinding.FragmentHomeBinding
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

//                outRect.left = 20.toPx()
//                outRect.right = 20.toPx()
                outRect.bottom = 15.toPx()


                when(parent.getChildLayoutPosition(view)) {
                    0 -> outRect.top = space
                    else -> outRect.top = 15.toPx()
                }
            }
        })


//        binding.recyclerHomeRecommend.adapter = RecommendRecyclerAdpater(viewModel)
//        binding.recyclerHomeMytrack.adapter = HomeBookRecyclerAdapter(viewModel)
//        binding.recyclerHomeMywork.adapter = HomeBookRecyclerAdapter(viewModel)
//
//        PagerSnapHelper().attachToRecyclerView(binding.recyclerHomeRecommend)
//
//        binding.recyclerHomeMytrack.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                val position = parent.getChildAdapterPosition(view)
//                outRect.left = 10.toPx()
//                outRect.right = 10.toPx()
//                outRect.top = 8.toPx()
//                outRect.bottom = 8.toPx()
//                when(position) {
//                    0 ->
//                    {
//                        outRect.left = 20.toPx()
//                    }
//                }
//            }
//        })

//        viewModel.onlyShowMostPopularBooks.observe(this, Observer {
//            it?.let {onlyShowMostPopularBooks ->
//                if (onlyShowMostPopularBooks) {
////                    val layoutManager = GridLayoutManager(context!!, 3, RecyclerView.VERTICAL, false)
////                    binding.recyclerHomeMytrack.layoutParams.
//                    val layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
//                    binding.recyclerHomeMytrack.layoutManager = layoutManager
//                } else {
//                    val layoutManager = GridLayoutManager(context!!, 1, RecyclerView.HORIZONTAL, false)
////                    binding.recyclerHomeMytrack.layoutManager
//                }
//            }
//        })


        binding.viewModel = viewModel

        viewModel.fetchDatas()
        return binding.root
    }
}