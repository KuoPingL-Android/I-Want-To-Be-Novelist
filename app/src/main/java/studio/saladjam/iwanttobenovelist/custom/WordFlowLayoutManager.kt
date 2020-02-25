package studio.saladjam.iwanttobenovelist.custom

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WordFlowLayout: RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        if (recycler != null) {
            /** Temporarily detach and scrap all currently attached child views */
            detachAndScrapAttachedViews(recycler)

            if (state?.itemCount ?: 0 <= 0) return
//            fillBottom(recycler, state.itemCount)

        } else {
            super.onLayoutChildren(recycler, state)
        }

    }
}