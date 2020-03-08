package studio.saladjam.iwanttobenovelist.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.databinding.DialogLoadingBinding
import studio.saladjam.iwanttobenovelist.factories.viewmodelfactories.DialogViewModelProviderFactory
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class LoadingDialog : AppCompatDialogFragment() {
    private lateinit var binding: DialogLoadingBinding
    /**
     * viewModels can only be accessed only after this Fragment is attached
     * therefore it will crash if we use this property
     * */
    private val viewModel by lazy {
        DialogViewModelProviderFactory().create(LoadingDialogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LoginDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLoadingBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    fun setMessage(message: String, status: ApiLoadingStatus) {
        viewModel.setMessage(message, status)
    }

}