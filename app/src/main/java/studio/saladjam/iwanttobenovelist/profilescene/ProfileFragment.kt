package studio.saladjam.iwanttobenovelist.profilescene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import studio.saladjam.iwanttobenovelist.databinding.FragmentProfileBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        binding.viewModel = viewModel

        viewModel.shouldCreateNewBook.observe(this, Observer {
            it?.let {

                viewModel.doneNavigateToNewBook()
            }
        })

        return binding.root
    }
}