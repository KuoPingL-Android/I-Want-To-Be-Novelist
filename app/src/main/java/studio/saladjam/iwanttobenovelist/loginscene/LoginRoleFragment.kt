package studio.saladjam.iwanttobenovelist.loginscene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginRoleBinding

class LoginRoleFragment(private val completeHandler: LoginPagesCompleteHandler) : LoginBaseSubFragment(completeHandler) {
    private lateinit var binding: FragmentLoginRoleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginRoleBinding.inflate(inflater, container, false)

        return binding.root
    }
}