package studio.saladjam.iwanttobenovelist.homescene.viewholders

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.setTouchDelegate
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.HomeWorkInProgressViewModel
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeWorkInProgressItemAdapter
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

class HomeWorkInProgressViewHolder(private val binding: ItemHomeV1Binding,
                                   private val workInProgressViewModel: HomeWorkInProgressViewModel) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val DECORATOR_RIGHT_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val DECORATOR_LEFT_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val DECORATE_END_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_LARGE
        private val DECORATE_START_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_LARGE

    }


    fun bind(sealItem: HomeSealItems.WorkInProgress, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.id
            title = sealItem.section.title
            badge = ""
            this.viewModel = viewModel

            /** SET ADAPTERs for RECYCLERVIEW */
            recyclerItemHomeV1.adapter =
                HomeWorkInProgressItemAdapter(
                    viewModel,
                    section,
                    workInProgressViewModel
                )
            (recyclerItemHomeV1.adapter as HomeWorkInProgressItemAdapter).submitList(sealItem.books)

            recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = DECORATOR_RIGHT_MARGIN
                    outRect.left = DECORATOR_LEFT_MARGIN

                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = DECORATE_START_MARGIN
                        }
                        (parent.adapter?.itemCount ?: 1) - 1 -> {
                            outRect.right = DECORATE_END_MARGIN
                        }
                    }
                }
            })
        }

        binding.textItemHomeV1Seeall.setTouchDelegate()

        binding.executePendingBindings()
    }
}