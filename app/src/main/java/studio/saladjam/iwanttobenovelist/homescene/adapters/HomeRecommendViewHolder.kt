package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1RecommendlistBinding
import studio.saladjam.iwanttobenovelist.extensions.getFloat
import studio.saladjam.iwanttobenovelist.extensions.setTouchDelegate
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import kotlin.math.absoluteValue

class HomeRecommendViewHolder (val binding: ItemHomeV1RecommendlistBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val RECYCLER_ITEM_START_PADDING
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_LARGE

        private val RECYCLER_ITEM_NORMAL_H_PADDING
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL

        private val BOOK_COVER_W_H_RATIO
                = IWBNApplication.instance
                    .getFloat(R.fraction.book_cover_w_to_h_ratio)

        private val BOOK_COVER_H_ITEM_H_RATIO
                = IWBNApplication.instance
                    .getFloat(R.fraction.home_recommend_item_cover_height_to_item_height)

        private val RECYCLER_ITEM_WH_RATIO
                = IWBNApplication.instance
                    .getFloat(R.fraction.home_recommend_item_scale)
    }

    fun bind(sealItem: HomeSealItems.Recommend, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.id
            title = sealItem.section.title
            badge = ""
            this.viewModel = viewModel

            /** SET ADAPTERs for RECYCLERVIEW */
//            when(sealItem.section) {
//                HomeSections.RECOMMEND -> {
//                    recyclerItemHomeV1.adapter = HomeRecommendItemAdapter(viewModel, section)
//                    (recyclerItemHomeV1.adapter as HomeRecommendItemAdapter).submitList(sealItem.books)
//
//                    recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
//                        override fun getItemOffsets(
//                            outRect: Rect,
//                            view: View,
//                            parent: RecyclerView,
//                            state: RecyclerView.State
//                        ) {
//                            outRect.right = 5.toPx()
//                            when(parent.getChildLayoutPosition(view)) {
//                                0 -> {
//                                    outRect.left = 20.toPx()
//                                }
//                                else -> {
//                                    outRect.left = 5.toPx()
//                                }
//                            }
//                        }
//                    })
//                }
//
//                HomeSections.POPULAR -> {
//
//                }
//            }

            recyclerItemHomeV1.adapter = HomeRecommendItemAdapter(viewModel, section)
            (recyclerItemHomeV1.adapter as HomeRecommendItemAdapter).submitList(sealItem.books)

            recyclerItemHomeV1.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    val layoutManager = recyclerView.layoutManager!!
                    val distance = (layoutManager.width.toFloat())

                    val startingPoint = RECYCLER_ITEM_START_PADDING.toFloat()

                    for (i in 0 until layoutManager.childCount) {
                        val child = layoutManager.getChildAt(i)!!
                        val childStartPoint
                                = (layoutManager.getDecoratedLeft(child) +
                                layoutManager.getDecoratedRight(child)) / 2f -
                                child.width/2f

                        val d = minOf(distance, (startingPoint - childStartPoint).absoluteValue)
                        val scale =
                            1 - RECYCLER_ITEM_WH_RATIO * d / distance
                        child.scaleX = scale
                        child.scaleY = scale
                    }
                }
            })

            recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {

                    outRect.right = RECYCLER_ITEM_NORMAL_H_PADDING
                    outRect.left = RECYCLER_ITEM_NORMAL_H_PADDING

                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = RECYCLER_ITEM_START_PADDING
                        }
                        (parent.adapter?.itemCount?: 1) - 1 -> {
                            outRect.right =
                                (parent.width - parent.height *
                                        BOOK_COVER_H_ITEM_H_RATIO *
                                        BOOK_COVER_W_H_RATIO -
                                        RECYCLER_ITEM_NORMAL_H_PADDING).toInt()
                        }
                    }
                }
            })


        }

        binding.textItemHomeV1Seeall.setTouchDelegate()

        binding.executePendingBindings()
    }
}