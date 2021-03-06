package studio.saladjam.iwanttobenovelist.homescene.viewholders

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.setTouchDelegate
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeGeneralItemAdapter
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

/** THIS IS THE VIEWHOLDER for HOME MAIN RECYCLER VIEW ITEMS INCLUDING
 *  RECOMMENDED and POPULAR
 * */

class HomeGeneralViewHolder(val binding: ItemHomeV1Binding) : RecyclerView.ViewHolder(binding.root) {

    

    fun bind(sealItem: HomeSealItems.General, viewModel: HomeViewModel, section: HomeSections) {

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


            recyclerItemHomeV1.adapter =
                HomeGeneralItemAdapter(
                    viewModel,
                    section
                )
            (recyclerItemHomeV1.adapter as HomeGeneralItemAdapter).submitList(sealItem.books)

            recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = 10.toPx()
                    outRect.left = 10.toPx()
                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = 20.toPx()
                        }
                        (parent.adapter?.itemCount ?: 1) - 1 -> {
                            outRect.right = 30.toPx()
                        }
                    }
                }
            })
        }

        binding.textItemHomeV1Seeall.setTouchDelegate()

        binding.executePendingBindings()
    }
}

