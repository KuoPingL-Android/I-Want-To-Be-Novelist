package studio.saladjam.iwanttobenovelist.categoryscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.saladjam.iwanttobenovelist.databinding.FragmentCategoryBinding
import studio.saladjam.iwanttobenovelist.databinding.FragmentCategoryV1Binding

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryV1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryV1Binding.inflate(inflater)



        return binding.root
    }
}