package studio.saladjam.iwanttobenovelist.profilescene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBookReadingBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.profilescene.adapters.ProfileBookReadingAdapter

class ProfileBookReadingFragment(private val profileViewModel: ProfileViewModel) : Fragment() {

    private lateinit var binding: FragmentProfileBookReadingBinding
    private lateinit var adapater: ProfileBookReadingAdapter
    private val viewModel by viewModels<ProfileBookReadingViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBookReadingBinding.inflate(inflater)

        binding.lifecycleOwner = this

        adapater = ProfileBookReadingAdapter(viewModel)

        binding.recyclerProfileBookmarks.adapter = adapater


        return binding.root
    }
}