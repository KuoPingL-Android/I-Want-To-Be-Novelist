package studio.saladjam.iwanttobenovelist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import studio.saladjam.iwanttobenovelist.databinding.ActivityMainBinding
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.APILoadingStatus


class MainActivity : AppCompatActivity() {

    enum class ToolBarBottomNavDisplays {
        DISPLAYTOOLBARONLY, DISPLAYBOTTOMNAVONLY, DISPLAYBOTH, HIDEBOTH
    }

    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Int = -1

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var nav: NavController

    private val bottomNavOnItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        val nav = findNavController(R.id.fragment_main)

        when(item.itemId) {
            R.id.navigation_home -> {
                viewModel.navigateToHomePage()
                true
            }

            R.id.navigation_category -> {
                viewModel.navigateToCategory()
                true
            }

            R.id.navigation_profile -> {
                viewModel.navigateToProfilePage()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        nav = findNavController(R.id.fragment_main)

        binding.bottomnavMain.setupWithNavController(nav)

        viewModel.shouldNavigateToHomePage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalHomeFragment())
                showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                viewModel.doneNavigateToHomePage()
            }
        })

        viewModel.shouldNavigateToCategoryPage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalCategoryFragment())
                showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                viewModel.doneNavigateToCategory()
            }
        })

        viewModel.shouldNavigateToProfilePage.observe(this, Observer {
            it?.let {

                if (IWBNApplication.user.token == null) {
                    //TODO: SHOW DIALOG TO SIGNUP
                } else {
                    nav.navigate(NavigationDirections.actionGlobalProfileFragment())
                    showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                }

                viewModel.doneNavigateToProfilePage()
            }
        })

        viewModel.shouldNavigateToLoginPage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalLoginFragment())
                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                viewModel.doneNavigateToLoginPage()
            }
        })

        viewModel.selectedBook.observe(this, Observer {
            it?.let {book ->
                nav.navigate(NavigationDirections.actionGlobalBookFragment(book))
                viewModel.doneNavigateToBook()
            }
        })

        viewModel.shouldCreateNewChapter.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalEditorFragment())
                showBars(ToolBarBottomNavDisplays.DISPLAYTOOLBARONLY)
                viewModel.doneNavigateToNewChapter()
            }
        })

        currentFragment = R.layout.fragment_login

        // NAVIGATION
        binding.bottomnavMain.setOnNavigationItemSelectedListener(bottomNavOnItemSelectedListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()


    }

    fun showAPIStatusDialog(status: APILoadingStatus) {
        when(status) {
            APILoadingStatus.LOADING -> {

            }
            else -> {

            }
        }
    }

    private fun showBars(value: ToolBarBottomNavDisplays) {
        when(value) {
            ToolBarBottomNavDisplays.DISPLAYTOOLBARONLY -> {
                showTopBar(true)
                showBottomBar(false)
            }

            ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY -> {
                showTopBar(false)
                showBottomBar(true)
            }

            ToolBarBottomNavDisplays.DISPLAYBOTH -> {
                showTopBar(true)
                showBottomBar(true)
            }

            ToolBarBottomNavDisplays.HIDEBOTH -> {
                showTopBar(false)
                showBottomBar(false)
            }

        }
    }

    private fun showTopBar(value: Boolean) {
        binding.toolbarMain.visibility = if (value) View.VISIBLE else View.GONE
    }

    private fun showBottomBar(value: Boolean) {
        binding.bottomnavMain.visibility = if (value) View.VISIBLE else View.GONE
    }
}
