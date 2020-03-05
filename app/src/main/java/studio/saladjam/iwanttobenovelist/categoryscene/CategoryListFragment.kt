package studio.saladjam.iwanttobenovelist.categoryscene

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.categoryscene.adapters.CategoryListAdapter
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryListBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre

class CategoryListFragment(val genre: Genre): Fragment() {

    companion object {
        private val RECYCLER_VIEW_ITEM_OFFSET_TOP
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val RECYCLER_VIEW_ITEM_OFFSET_RIGHT
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val RECYCLER_VIEW_ITEM_OFFSET_LEFT
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val RECYCLER_VIEW_ITEM_OFFSET_BOTTOM
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
    }

    private lateinit var binding: ItemCategoryListBinding
    private val viewModel
            by viewModels<CategoryListViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemCategoryListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.selectedBook.observe(this, Observer {
            it?.let {
                //TODO:
            }
        })

        binding.viewModel = viewModel

        binding.recyclerItemCategory.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {

                outRect.right   = RECYCLER_VIEW_ITEM_OFFSET_RIGHT
                outRect.left    = RECYCLER_VIEW_ITEM_OFFSET_LEFT
                outRect.top     = RECYCLER_VIEW_ITEM_OFFSET_TOP
                outRect.bottom  = RECYCLER_VIEW_ITEM_OFFSET_BOTTOM
            }
        })

        binding.recyclerItemCategory.adapter = CategoryListAdapter(viewModel)

        viewModel.fetchBooksOn(genre, "")

        return binding.root
    }
}