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

        /** LOGIN PAGE */
        viewModel.shouldNavigateToLoginPage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalLoginFragment())
                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                viewModel.doneNavigateToLoginPage()
            }
        })

        /** HOME PAGE */
        viewModel.shouldNavigateToHomePage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalHomeFragment())
                showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                viewModel.doneNavigateToHomePage()
            }
        })

        /** CATEGORY SCENE */
        viewModel.shouldNavigateToCategoryPage.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalCategoryFragment())
                showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                viewModel.doneNavigateToCategory()
            }
        })

        /** PROFILE SCENE */
        viewModel.shouldNavigateToProfilePage.observe(this, Observer {
            it?.let {

                if (IWBNApplication.user.token == null) {
                    //TODO: SHOW DIALOG TO SIGNUP
                    viewModel.doneNavigateToProfilePage()
                    viewModel.navigateToLoginPage()
                } else {
                    nav.navigate(NavigationDirections.actionGlobalProfileFragment())
                    showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                }

                viewModel.doneNavigateToProfilePage()
            }
        })

        /** BOOK SCENE */
        viewModel.selectedBook.observe(this, Observer {
            it?.let {book ->
                nav.navigate(NavigationDirections.actionGlobalBookFragment(book))
                viewModel.doneNavigateToBook()
            }
        })

        /** BOOK EDITING SCENE */
        viewModel.selectBookToEdit.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalBookWriteDetailFragment(it))
                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                viewModel.doneDisplayingEditingBook()
            }
        })

        /** EDITOR SCENE -> EDITOR TEXT FRAGMENT */
        viewModel.selectedChapterForEditing.observe(this, Observer {
            it?.let {
                nav.navigate(NavigationDirections.actionGlobalEditorTextFragment(it))
                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                viewModel.doneNavigateToEditor()
            }
        })

        /** EDITOR SCENE -> EDITOR MIXER FRAGMENT */
        viewModel.selectedChapterForModifcation.observe(this, Observer {
            it?.let {chapter ->
                nav.navigate(NavigationDirections.actionGlobalEditorMixerFragment(chapter))
                viewModel.doneNavigationToModify()
            }
        })

        /** BOOK READING FRAGMENT */
        //TODO: IMPLEMENT
        viewModel.selectedBookToRead.observe(this, Observer {
            it?.let {
                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                viewModel.doneDisplayingReadingBook()
            }
        })

        currentFragment = R.layout.fragment_login

        // NAVIGATION
        binding.bottomnavMain.setOnNavigationItemSelectedListener(bottomNavOnItemSelectedListener)
    }

    private fun setupOnNaigateListener() {
        nav.addOnDestinationChangedListener { controller, destination, arguments ->
            viewModel.currentFragmentType.value =  when(controller.currentDestination?.id) {
                R.id.loginFragment -> CurrentFragmentType.LOGIN
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.categoryFragment -> CurrentFragmentType.CATEGORY
                R.id.bookFragment  -> CurrentFragmentType.BOOKDETAIL
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                else -> viewModel.currentFragmentType.value
            }
        }
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
