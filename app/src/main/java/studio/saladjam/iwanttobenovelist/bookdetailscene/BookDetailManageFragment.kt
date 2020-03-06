package studio.saladjam.iwanttobenovelist.bookdetailscene

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.bookdetailscene.adapters.BookDetailManageAdapter
import studio.saladjam.iwanttobenovelist.constants.NavArgKeys
import studio.saladjam.iwanttobenovelist.constants.TimeConstants
import studio.saladjam.iwanttobenovelist.databinding.FragmentBookDetailManageBinding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory
import studio.saladjam.iwanttobenovelist.repository.dataclass.Book
import studio.saladjam.iwanttobenovelist.repository.dataclass.Categories
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus

class BookDetailManageFragment : Fragment() {

    private lateinit var binding: FragmentBookDetailManageBinding
    private val book by lazy {
        requireArguments().get(NavArgKeys.BOOK) as Book
    }
    private val viewModel by viewModels<BookDetailManageViewModel> { getVMFactory() }
    private var mainViewModel: MainViewModel? = null

    private lateinit var category: Categories

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailManageBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerBookDetailChapters.adapter = BookDetailManageAdapter(viewModel)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {

                if (binding.spinnerBookDetailCategory.adapter == null) {

                    category = it

                    val categories = it.getDisplayNames().toMutableList()

                    val ids = it.genres.map { it.id }

                    if (ids.contains(book.category)) {
                        val index = ids.indexOf(book.category)
                        CoroutineScope(Dispatchers.Main).launch {
//                            binding.spinnerBookDetailCategory.prompt = categories[index]
                            binding.spinnerBookDetailCategory.setSelection(index, true)
                        }
                    }

                    binding.spinnerBookDetailCategory.adapter = object : ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item,
                        categories) {
                        //TODO: SET PROPER LANGUAGE

                    }

                    binding.spinnerBookDetailCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedCategory = parent?.getItemAtPosition(position) as String
                            viewModel.book.value?.category = category.getGenreIDFor(selectedCategory)
                        }

                    }
                }
            }
        })

        viewModel.dialogInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel?.displayLoadingDialog(it.first, it.second)

                if(it.second == ApiLoadingStatus.DONE) {
                    if (viewModel.isSaving) {
                        Handler().postDelayed({
                            viewModel.isSaving = false
                            findNavController().navigateUp()
                        }, TimeConstants.TIME_1000_ms)
                    }
                }

                viewModel.doneDisplayingDialogInfo()
            }
        })

        viewModel.shouldNavigateBack.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigateBack()
            }
        })

        viewModel.prepareBook(book)

        return binding.root
    }
}