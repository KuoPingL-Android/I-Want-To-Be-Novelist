package studio.saladjam.iwanttobenovelist.profilescene

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.constants.RecyclerViewConstants
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBookReadingBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileBookReadingAdapter

class ProfileBookReadingFragment(private val profileViewModel: ProfileViewModel) : Fragment() {

    companion object {
        private val ITEM_DECORATOR_NORMAL_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_NORMAL
        private val ITEM_DECORATOR_ENDS_MARGIN
                = RecyclerViewConstants.ITEM_DECORATOR_MARGIN_LARGE
    }

    private lateinit var binding: FragmentProfileBookReadingBinding
    private lateinit var adapter: ProfileBookReadingAdapter
    private val viewModel by viewModels<ProfileBookReadingViewModel> { getVMFactory() }
    private var mainViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBookReadingBinding.inflate(inflater)

        binding.lifecycleOwner = this

        adapter = ProfileBookReadingAdapter(viewModel)

        binding.recyclerProfileBookmarks.adapter = adapter

        binding.recyclerProfileBookmarks.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.indexOfChild(view)

                outRect.top     = ITEM_DECORATOR_NORMAL_MARGIN
                outRect.bottom  = ITEM_DECORATOR_NORMAL_MARGIN

                when(position) {
                    0 -> outRect.top = ITEM_DECORATOR_ENDS_MARGIN
                    parent.childCount - 1 -> outRect.bottom = ITEM_DECORATOR_ENDS_MARGIN
                }
            }
        })

        binding.viewModel = viewModel

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.selectedBookToRead(it)
                viewModel.doneNavigateToBook()
            }
        })

        viewModel.fetchBookList()

        return binding.root
    }
}