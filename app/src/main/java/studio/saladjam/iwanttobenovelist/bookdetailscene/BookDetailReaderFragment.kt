package studio.saladjam.iwanttobenovelist.bookdetailscene

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
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailReaderAdapter
import studio.saladjam.iwanttobenovelist.databinding.FragmentBookBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookDetailReaderFragment : Fragment() {
    private val viewModel by viewModels<BookDetailViewModel> { getVMFactory() }

    private lateinit var binding: FragmentBookBinding
    private lateinit var mainviewModel: MainViewModel
    private var book: Book? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerBookReadDetail.adapter = BookDetailReaderAdapter(viewModel)

        mainviewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.shouldGoBackToPreviousPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingToPreviousPage()
            }
        })

        viewModel.selectedChapterToRead.observe(viewLifecycleOwner, Observer {
            it?.let {
                book?.let {book ->
                    mainviewModel.selectChpaterToReadInBook(Pair(book, it))
                }
                viewModel.doneNavigateToReader()
            }
        })


        book = requireArguments().get("book") as? Book

        book?.let {
            viewModel.book = it
            viewModel.fetchChapters()
        }

        return binding.root
    }
}