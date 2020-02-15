package studio.saladjam.iwanttobenovelist.editorscene

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.bind
import studio.saladjam.iwanttobenovelist.databinding.FragmentEditorTextBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.extensions.toPx
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus

class EditorTextFragment : Fragment() {
    private lateinit var binding: FragmentEditorTextBinding
    private var chapter: Chapter? = null
    private val viewModel by viewModels<EditorTextViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditorTextBinding.inflate(inflater)
        binding.lifecycleOwner = this

        chapter = requireArguments().get("chapter") as? Chapter
        chapter?.let {
            viewModel.prepareChapter(it)
        }

        binding.editEditorTextTitle.onFocusChangeListener= View.OnFocusChangeListener{v, hasFocus ->
            if(hasFocus) {
                if(binding.editEditorTextTitle.text.isEmpty()) {
                    binding.editEditorTextTitle.hint = ""
                }
            } else {
                if (binding.editEditorTextTitle.text.isEmpty()) {
                    binding.editEditorTextTitle.hint = IWBNApplication.context.resources.getString(R.string.hint_book_title)
                }
            }
        }

        viewModel.error.observe(this, Observer {
            it?.let {message ->
                if (message.isNotEmpty() && viewModel.status.value == APILoadingStatus.ERROR) {
                    Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
                }
                viewModel.doneDisplayingError()
            }
        })

        viewModel.shouldGoBackToPreviousPage.observe(this, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingToPreviousPage()
            }
        })

        binding.viewModel = viewModel

        return binding.root
    }
}