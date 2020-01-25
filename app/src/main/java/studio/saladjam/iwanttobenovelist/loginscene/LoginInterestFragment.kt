package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginInterestBinding

class LoginInterestFragment(private val completeHandler: LoginPagesCompleteHandler)
    : LoginBaseSubFragment(completeHandler) {
    private lateinit var binding: FragmentLoginInterestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginInterestBinding.inflate(inflater)
        return binding.root
    }
}