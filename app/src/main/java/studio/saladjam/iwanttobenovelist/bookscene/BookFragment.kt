package studio.saladjam.iwanttobenovelist.bookscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import studio.saladjam.iwanttobenovelist.databinding.FragmentBookBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class BookFragment(val book: Book) : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private val viewModel by viewModels<BookViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.book = book
        viewModel.setBook(book)
        return binding.root
    }
}