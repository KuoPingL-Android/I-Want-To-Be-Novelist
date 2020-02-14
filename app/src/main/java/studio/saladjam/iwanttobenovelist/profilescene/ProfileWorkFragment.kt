package studio.saladjam.iwanttobenovelist.profilescene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileWorksBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
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

        viewModel.selectBookForDisplayDetail.observe(this, Observer {
            it?.let {
                mainViewModel.displayEditingBook(it)
                viewModel.doneShowingBookDetail()
            }
        })

        viewModel.fetchUserWork()


        return binding.root
    }
}