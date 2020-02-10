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
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileRecyclerViewAdapter

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.shouldCreateNewBook.observe(this, Observer {
            it?.let {
                ViewModelProviders.of(activity!!).get(MainViewModel::class.java).createNewChapter()
                viewModel.doneNavigateToNewBook()
            }
        })

        binding.recyclerProfile.adapter = ProfileRecyclerViewAdapter(viewModel)
        (binding.recyclerProfile.adapter as ProfileRecyclerViewAdapter).submitList(mutableListOf("1", "2"))

        LinearSnapHelper().attachToRecyclerView(binding.recyclerProfile)


        return binding.root
    }
}