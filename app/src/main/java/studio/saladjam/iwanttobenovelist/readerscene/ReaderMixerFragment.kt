package studio.saladjam.iwanttobenovelist.readerscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import studio.saladjam.iwanttobenovelist.databinding.FragmentReaderMixerBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book

class ReaderMixerFragment : Fragment() {
    private lateinit var binding: FragmentReaderMixerBinding
    private val book = requireArguments().get("book") as Book
    private val viewModel by viewModels<ReaderMixerViewModel> { getVMFactory(book)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReaderMixerBinding.inflate(inflater)



        return binding.root
    }
}