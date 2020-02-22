package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1RecommendlistBinding
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems
import kotlin.math.absoluteValue

class HomeRecommendViewHolder (val binding: ItemHomeV1RecommendlistBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(sealItem: HomeSealItems.Recommend, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.value
            title = sealItem.title
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

                    val startingPoint = 20.toPx().toFloat()

                    for (i in 0 until layoutManager.childCount) {
                        val child = layoutManager.getChildAt(i)!!
                        val childStartpoint = (layoutManager.getDecoratedLeft(child) + layoutManager.getDecoratedRight(child)) / 2f - child.width/2f

                        val d = minOf(distance, (startingPoint - childStartpoint).absoluteValue)
                        val scale = 1 + -0.32 * d / distance
                        child.scaleX = scale.toFloat()
                        child.scaleY = scale.toFloat()
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
                    outRect.right = 10.toPx()
                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = 20.toPx()
                        }
                        (parent.adapter?.itemCount?: 1) - 1 -> {
                            outRect.right = parent.width/2 - parent.width/6
                        }
                        else -> {
                            outRect.left = 10.toPx()
                        }
                    }
                }
            })


        }

        binding.executePendingBindings()
    }
}