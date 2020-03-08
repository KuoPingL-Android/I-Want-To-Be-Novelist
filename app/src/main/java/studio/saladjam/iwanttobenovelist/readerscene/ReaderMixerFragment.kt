package studio.saladjam.iwanttobenovelist.readerscene

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
import studio.saladjam.iwanttobenovelist.constants.NavArgKeys
import studio.saladjam.iwanttobenovelist.databinding.FragmentReaderMixerBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class ReaderMixerFragment : Fragment() {
    private lateinit var binding: FragmentReaderMixerBinding
    private lateinit var book: Book
    private lateinit var chapter: Chapter
    private val viewModel by viewModels<ReaderMixerViewModel> { getVMFactory(book)}
    private var mainViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReaderMixerBinding.inflate(inflater)
        binding.lifecycleOwner = this

        book = requireArguments().get(NavArgKeys.BOOK) as Book
        chapter = requireArguments().get(NavArgKeys.CHAPTER) as Chapter

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        prepareViewModel()

        return binding.root
    }

    private fun prepareViewModel() {
//        viewModel.displayChapter(chapter)

        viewModel.currentChapter.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.simpleContainer.setContentWithPaint(it, binding.editEditorMix.paint)
            }
        })


        viewModel.currentChapterBlock.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.simpleContainer.addBlocks(it)
            }
        })

        viewModel.shouldNavigateBackToPreviousPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigateToPreviousPage()
            }
        })

        viewModel.fetchChapterDetails(chapter.chapterIndex)
    }
}