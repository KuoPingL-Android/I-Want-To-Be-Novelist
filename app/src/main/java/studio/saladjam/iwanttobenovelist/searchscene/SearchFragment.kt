package studio.saladjam.iwanttobenovelist.searchscene

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.FragmentSearchBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchFilterAdapter
import studio.saladjam.iwanttobenovelist.searchscene.adapters.SearchResultAdapter

class SearchFragment : Fragment() {

    companion object {
        private val ITEM_DECORATOR_NORMAL_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_SMALL_8dp
        private val ITEM_DECORATOR_ENDS_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL_8dp
    }

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel> { getVMFactory() }
    private lateinit var filterAdapter: SearchFilterAdapter
    private lateinit var resultAdapter: SearchResultAdapter
    private var mainViewModel: MainViewModel? = null
    private var initialFilter: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        filterAdapter = SearchFilterAdapter(viewModel)
        resultAdapter = SearchResultAdapter(viewModel)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        binding.recyclerSearchFilters.adapter = filterAdapter
        binding.recyclerSearchResult.adapter = resultAdapter

        binding.searchCategorySearchview.setBackgroundColor(resources.getColor(android.R.color.transparent))

        binding.viewModel = viewModel

        viewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.selectedBookToRead(it)
                viewModel.doneSelectingBook()
            }
        })

        binding.recyclerSearchFilters.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {

                outRect.left = ITEM_DECORATOR_NORMAL_MARGIN
                outRect.right = ITEM_DECORATOR_NORMAL_MARGIN

                when(parent.getChildLayoutPosition(view)) {
                    0
                        -> outRect.left = ITEM_DECORATOR_ENDS_MARGIN
                    (parent.adapter?.itemCount?: 1) - 1
                        -> outRect.right = ITEM_DECORATOR_ENDS_MARGIN
                }

            }
        })


        binding.searchCategorySearchview.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.makeSearchOnText(it)
                }
                return true
            }

        })

        initialFilter = requireArguments().get("searchFilter") as Int
//            binding.recyclerSearchFilters.
        viewModel.select(initialFilter!!.getSearchFilter())

        return binding.root
    }
}