package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.setTouchDelegate
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

class HomeCurrentReadViewHolder(val binding: ItemHomeV1Binding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(sealItem: HomeSealItems.CurrentReading, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.value
            title = sealItem.title
            badge = ""
            this.viewModel = viewModel
            textItemHomeV1Title.setTextColor(Color.parseColor("#000000"))
            textItemHomeV1Seeall.setTextColor(Color.parseColor("#000000"))

            /** SET ADAPTERs for RECYCLERVIEW */
            recyclerItemHomeV1.adapter = HomeCurrentReadItemAdapter(viewModel, section)
            (recyclerItemHomeV1.adapter as HomeCurrentReadItemAdapter).submitList(sealItem.books)

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