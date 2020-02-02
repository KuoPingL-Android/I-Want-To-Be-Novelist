package studio.saladjam.iwanttobenovelist.categoryscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import studio.saladjam.iwanttobenovelist.categoryscene.adapters.CategoryGenreViewPagerAdapter
import studio.saladjam.iwanttobenovelist.databinding.FragmentCategoryV1Binding
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryV1Binding
    private val viewModel by viewModels<CategoryViewModel> { getVMFactory() }
    private lateinit var viewPagerAdapter: CategoryGenreViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryV1Binding.inflate(inflater)

        viewPagerAdapter = CategoryGenreViewPagerAdapter(childFragmentManager)
        binding.viewpagerCategory.adapter = viewPagerAdapter
        binding.tablayoutCategoryCategories.setupWithViewPager(binding.viewpagerCategory)

        viewModel.category.observe(this, Observer {
            it?.let {genres ->
                viewPagerAdapter.setGenres(genres)
            }
        })

        viewModel.fetchCategories()


        return binding.root
    }
}