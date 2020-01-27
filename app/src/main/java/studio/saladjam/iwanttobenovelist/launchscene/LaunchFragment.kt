package studio.saladjam.iwanttobenovelist.launchscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentLaunchBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory

class LaunchFragment : Fragment() {
    private lateinit var binding: FragmentLaunchBinding
    private val viewModel by viewModels<LaunchViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.shouldNavigateToHome.observe(this, Observer {
            it?.let {
                mainViewModel.navigateToHomePage()
                viewModel.doneNavigatingToHome()
            }
        })

        viewModel.shouldNavigateToLogin.observe(this, Observer {
            it?.let {
                mainViewModel.navigateToLoginPage()
                viewModel.doneNavigatingToLogin()
            }
        })

        viewModel.checkIfFirstTimeUser()

        return binding.root
    }
}