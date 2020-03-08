package studio.saladjam.iwanttobenovelist.loginscene

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import studio.saladjam.iwanttobenovelist.IWBNApplication
import studio.saladjam.iwanttobenovelist.Logger
import studio.saladjam.iwanttobenovelist.MainViewModel
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.constants.TimeConstants
import studio.saladjam.iwanttobenovelist.databinding.FragmentLoginSigninupBinding
import studio.saladjam.iwanttobenovelist.extensions.getStringArray
import studio.saladjam.iwanttobenovelist.extensions.getVMFactory


private const val RC_SIGN_IN = 1

class LoginSigninupFragment(private val completeHandler: LoginPagesCompleteHandler) : LoginBaseSubFragment(completeHandler) {

    companion object {
        private val NAME_REQUEST_DIALOG_TAG = "selectname"

    }

    private lateinit var binding: FragmentLoginSigninupBinding
    private val viewModel by viewModels<LoginSigninupViewModel> { getVMFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginSigninupBinding.inflate(inflater, container, false)

        val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        /** API STATUS */
        viewModel.dialogInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel.displayLoadingDialog(it.first, it.second)
                viewModel.doneDisplayingDialog()
            }
        })

        viewModel.loginWithFacebook.observe(this, Observer{
            it?.let{
                // Login with Facebook
                LoginManager.getInstance()
                    .logIn(this,
                        IWBNApplication.instance.getStringArray(R.array.fb_requests))
                viewModel.doneLoggingInWithFacebook()
            }
        })

        viewModel.loginWithGoogle.observe(this, Observer{
            it?.let{

                // Build a GoogleSignInClient with the options specified by gso.
                context?.let {context ->
                    viewModel.mGoogleSignInClient = GoogleSignIn
                        .getClient(context, IWBNApplication.container.googleSigninOptions)
                    val intent = viewModel.mGoogleSignInClient?.signInIntent
                    startActivityForResult(intent!!, RC_SIGN_IN)
                }
                viewModel.doneLoggingInWithGoogle()
            }
        })

        // This includes the action from Login as Visitor
        viewModel.shouldNavigateToHomePage.observe(this, Observer {
            it?.let{
                // Navigate to Home Page
                mainViewModel.navigateToHomePage()
                viewModel.doneNavigateToHomePage()
            }
        })

        viewModel.shouldNavigateToNextLoginPage.observe(this, Observer {
            it?.let{
                viewModel.doneNavigateToNextLoginPage()
                completeHandler(it)
            }
        })

        viewModel.shouldAskForName.observe(this, Observer {
            it?.let{
                LoginSelectNameDialog{wrappedName ->
                    wrappedName?.let {name ->
                        Handler().postDelayed({
                            viewModel.updateUserName(name)
                        }, TimeConstants.TIME_1000_ms)
                    }
                }.show(fragmentManager!!, NAME_REQUEST_DIALOG_TAG)
                viewModel.doneAskingForName()
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleGoogleTask(task)
        } else {
            viewModel.fbCallBackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}