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
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileWorksBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileWorkAdapter

class ProfileWorkFragment(private val profileViewModel: ProfileViewModel): Fragment() {

    private lateinit var binding: FragmentProfileWorksBinding
    private lateinit var adapter: ProfileWorkAdapter
    private val viewModel by viewModels<ProfileWorkViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileWorksBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.recyclerProfileWork.adapter = ProfileWorkAdapter(viewModel)
        binding.viewModel = viewModel

        val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        //TODO: SETUP RECYCLER VIEW DATA
        viewModel.shouldCreateNewBook.observe(this, Observer {
            it?.let {
                val profileCreateBookDialog = ProfileCreateBookDialog(viewModel)
                fragmentManager?.let {fm ->
                    profileCreateBookDialog.show(fm, "createBookDialog")
                }
                viewModel.doneShowingCreateBookDialog()
            }
        })


        binding.recyclerProfileWork.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.indexOfChild(view)

                outRect.top = 10.toPx()
                outRect.bottom = 10.toPx()

                when(position) {
                    0 -> outRect.top = 20.toPx()
                    parent.childCount - 1 -> outRect.bottom = 20.toPx()
                }
            }
        })

        viewModel.selectBookForDisplayDetail.observe(this, Observer {
            it?.let {
                mainViewModel.selectedBookToEdit(it)
                viewModel.doneShowingBookDetail()
            }
        })



        viewModel.fetchUserWork()


        return binding.root
    }
}