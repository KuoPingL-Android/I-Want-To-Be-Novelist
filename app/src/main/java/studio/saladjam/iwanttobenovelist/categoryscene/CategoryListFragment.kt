package studio.saladjam.iwanttobenovelist.categoryscene

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
import studio.saladjam.iwanttobenovelist.databinding.ItemCategoryListBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre

class CategoryListFragment(val genre: Genre): Fragment() {

    private lateinit var binding: ItemCategoryListBinding
    private val viewModel by viewModels<CategoryListViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemCategoryListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.selectedBook.observe(this, Observer {
            it?.let {
                //
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
                val position = parent.getChildLayoutPosition(view)

                outRect.right = 10.toPx()
                outRect.top = 10.toPx()
                outRect.bottom = 10.toPx()

                when(position%3) {
                    0 -> {
                        outRect.left = 10.toPx()
                    }
                    else -> {
                        outRect.left = 10.toPx()
                    }
                }
            }
        })

        binding.recyclerItemCategory.adapter = CategoryListAdapter(viewModel)

        viewModel.fetchBooksOn(genre, "")

        return binding.root
    }
}