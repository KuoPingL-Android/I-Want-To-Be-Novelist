package studio.saladjam.iwanttobenovelist.homescene.adapters

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.databinding.ItemHomeV1Binding
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.homescene.HomeSections
import studio.saladjam.iwanttobenovelist.homescene.HomeViewModel
import studio.saladjam.iwanttobenovelist.homescene.sealitems.HomeSealItems

class HomeGeneralViewHolder(val binding: ItemHomeV1Binding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(sealItem: HomeSealItems.General, viewModel: HomeViewModel, section: HomeSections) {

        binding.apply {
            homeSection = sealItem.section.value
            title = sealItem.title
            badge = ""
            this.viewModel = viewModel

            if(section == HomeSections.RECOMMEND) {
                textItemHomeV1Title.setTextColor(Color.parseColor("#ffffff"))
                textItemHomeV1Seeall.setTextColor(Color.parseColor("#ffffff"))
            }
            else {
                textItemHomeV1Title.setTextColor(Color.parseColor("#000000"))
                textItemHomeV1Seeall.setTextColor(Color.parseColor("#000000"))
            }

            /** SET ADAPTERs for RECYCLERVIEW */
            recyclerItemHomeV1.adapter = HomeGeneralItemAdapter(viewModel, section)
            (recyclerItemHomeV1.adapter as HomeGeneralItemAdapter).submitList(sealItem.books)

            recyclerItemHomeV1.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = 5.toPx()
                    when(parent.getChildLayoutPosition(view)) {
                        0 -> {
                            outRect.left = 20.toPx()
                        }
                        else -> {
                            outRect.left = 5.toPx()
                        }
                    }
                }
            })
        }

        binding.executePendingBindings()
    }
}

