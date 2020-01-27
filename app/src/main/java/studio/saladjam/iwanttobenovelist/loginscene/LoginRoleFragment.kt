package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginRoleBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import java.util.logging.Handler

class LoginRoleFragment(private val completeHandler: LoginPagesCompleteHandler) : LoginBaseSubFragment(completeHandler) {
    private lateinit var binding: FragmentLoginRoleBinding

    private val viewModel by viewModels<LoginRoleViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginRoleBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.shouldAskForName.observe(this, Observer {
            it?.let{
                LoginSelectNameDialog{wrappedName ->
                    wrappedName?.let {name ->
                        android.os.Handler().postDelayed({
                            viewModel.updateUserName(name)
                        }, 1000)
                    }
                }.show(fragmentManager!!, "selectname")
                viewModel.doneAskingForName()
            }
        })

        viewModel.shouldToGoNextPage.observe(this, Observer {
            it?.let(completeHandler)
        })

        return binding.root
    }
}