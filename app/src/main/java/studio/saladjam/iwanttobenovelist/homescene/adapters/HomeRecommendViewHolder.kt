package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1RecommendlistBinding
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

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