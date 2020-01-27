package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.DialogLoginSelectNameBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory

class LoginSelectNameDialog(val completeHandler: (name: String?)->Unit) : AppCompatDialogFragment() {

    private val viewModel by viewModels<LoginSelectNameViewModel> { getVMFactory() }

    private lateinit var binding: DialogLoginSelectNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MessageDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLoginSelectNameBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.shouldDismiss.observe(this, Observer {
            it?.let {
                completeHandler(viewModel.userName.value)
                viewModel.doneDismiss()
                dismiss()
            }
        })

        return binding.root
    }

}