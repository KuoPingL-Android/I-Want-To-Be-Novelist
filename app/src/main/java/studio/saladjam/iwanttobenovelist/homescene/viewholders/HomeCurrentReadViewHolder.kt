package studio.saladjam.iwanttobenovelist.homescene.viewholders

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.getPixelSize
import studio.saladjam.iwanttobenovelist.extensions.getScale
import studio.saladjam.iwanttobenovelist.extensions.setTouchDelegate
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.adapters.HomeCurrentReadItemAdapter
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

class HomeCurrentReadViewHolder(val binding: ItemHomeV1Binding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val ITEM_DECORATOR_NORMAL_MARGIN = IWBNApplication.instance
            .getPixelSize(R.dimen.item_decorator_normal_margin)
        private val ITEM_DECORATOR_ENDS_MARGIN = IWBNApplication.instance
            .getPixelSize(R.dimen.item_decorator_large_margin)
    }

    fun bind(sealItem: HomeSealItems.CurrentReading, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.id
            title = sealItem.section.title
            badge = ""
            this.viewModel = viewModel

            /** SET ADAPTERs for RECYCLERVIEW */
            recyclerItemHomeV1.adapter =
                HomeCurrentReadItemAdapter(
                    viewModel,
                    section
                )
            (recyclerItemHomeV1.adapter as HomeCurrentReadItemAdapter).submitList(sealItem.books)

            recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = ITEM_DECORATOR_NORMAL_MARGIN
                    outRect.left = ITEM_DECORATOR_NORMAL_MARGIN
                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = ITEM_DECORATOR_ENDS_MARGIN
                        }
                        (parent.adapter?.itemCount ?: 1) - 1 -> {

                            val scale = IWBNApplication.instance.getScale(R.string.home_card_w_to_h_ratio)
                            val itemWidth = parent.height * scale

                            outRect.right =
                                (parent.width - ITEM_DECORATOR_NORMAL_MARGIN - itemWidth).toInt()
                        }
                    }
                }
            })
        }

        binding.textItemHomeV1Seeall.setTouchDelegate()

        binding.executePendingBindings()
    }
}