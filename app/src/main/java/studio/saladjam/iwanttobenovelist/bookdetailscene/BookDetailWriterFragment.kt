package studio.saladjam.iwanttobenovelist.bookdetailscene

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import studio.saladjam.iwanttobenovelist.MainActivity
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailWriterAdpater
import studio.saladjam.iwanttobenovelist.databinding.FragmentBookWriterDetailBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookDetailWriterFragment : Fragment() {
    private val viewModel by viewModels<BookDetailWriterViewModel> { getVMFactory() }

    private lateinit var binding: FragmentBookWriterDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookWriterDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.recyclerBookWriterDetail.adapter = BookDetailWriterAdpater(viewModel)

        binding.viewModel = viewModel

        viewModel.book = requireArguments().get("book") as? Book

        viewModel.selectedChapter.observe(this, Observer {
            it?.let {
                viewModel.doneNavigatingToChapter()
            }
        })

        viewModel.shouldAddChapter.observe(this, Observer {
            it?.let {
                (ViewModelProviders.of(activity!! as MainActivity).get(MainViewModel::class.java))
                viewModel.donePreparingNewChapter()
            }
        })

        viewModel.fetchChapters()
        return binding.root
    }
}