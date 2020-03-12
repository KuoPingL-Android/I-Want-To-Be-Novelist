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
import studio.saladjam.iwanttobenovelist.MainActivity
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailWriterAdapter
import studio.saladjam.iwanttobenovelist.bookdetailscene.viewmodel.BookDetailViewModel
import studio.saladjam.iwanttobenovelist.bookdetailscene.viewmodel.ChapterDetailViewModel
import studio.saladjam.iwanttobenovelist.databinding.FragmentBookWriterDetailBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class BookDetailWriterFragment : Fragment() {
    private val viewModel by viewModels<BookDetailViewModel> { getVMFactory() }
    private val chapterViewModel by viewModels<ChapterDetailViewModel> { getVMFactory() }

    private lateinit var binding: FragmentBookWriterDetailBinding
    private lateinit var mainviewModel: MainViewModel
    private var book: Book? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookWriterDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.recyclerBookWriteDetail.adapter = BookDetailWriterAdapter(viewModel, chapterViewModel)

        binding.viewModel = viewModel

        mainviewModel = (ViewModelProviders.of(activity!! as MainActivity).get(MainViewModel::class.java))

        book = requireArguments().get("book") as Book

        viewModel.setBook(requireArguments().get("book") as Book)

        viewModel.selectedChapterToEdit.observe(this, Observer {
            it?.let {
                mainviewModel.selectChapterToEdit(it)
                viewModel.doneNavigatingToChapter()
            }
        })

        viewModel.shouldPromptForDetails.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })

        viewModel.shouldAddChapter.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.book.value?.let { unwrappedBook ->
                    val chapter = Chapter(
                        bookID = unwrappedBook.bookID,
                        chapterIndex = unwrappedBook.chapterCount,
                        title = "Chapter ${unwrappedBook.chapterCount + 1}")
                    mainviewModel.selectChapterToEdit(chapter)
                }
                viewModel.donePreparingNewChapter()
            }
        })

        viewModel.shouldGoBackToPreviousPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingToPreviousPage()
            }
        })

        viewModel.shouldShowBookDetailEditor.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainviewModel.selectBookToManage(book!!)
                viewModel.doneShowingEditorForBookDetail()
            }
        })

        viewModel.dialogInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainviewModel.displayLoadingDialog(it.first, it.second)
                viewModel.doneDisplayingDialog()
            }
        })

        viewModel.checkBookInfo()
        return binding.root
    }
}