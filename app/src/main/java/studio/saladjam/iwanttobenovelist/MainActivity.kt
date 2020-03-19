package studio.saladjam.iwanttobenovelist

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import studio.saladjam.iwanttobenovelist.constants.TimeConstants
import studio.saladjam.iwanttobenovelist.databinding.ActivityMainBinding
import studio.saladjam.iwanttobenovelist.dialog.LoadingDialog
import studio.saladjam.iwanttobenovelist.repository.loadingstatus.ApiLoadingStatus
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters


class MainActivity : AppCompatActivity() {

    enum class ToolBarBottomNavDisplays {
        DISPLAYTOOLBARONLY, DISPLAYBOTTOMNAVONLY, DISPLAYBOTH, HIDEBOTH
    }

    private lateinit var binding: ActivityMainBinding
    private var loadingDialog: LoadingDialog? = null
    private var currentFragment: Int = -1

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var nav: NavController

    private val bottomNavOnItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->

        when(item.itemId) {
            R.id.navigation_home -> {
                viewModel.navigateToHomePage()
                true
            }

            R.id.navigation_search -> {
                if(viewModel.shouldNavigateToSearchRecommend.value == null
                    && viewModel.shouldNavigateToSearchPopular.value == null) {
                    viewModel.navigateToSearchPopular()
                }
                true
            }

            R.id.navigation_profile -> {

                if (IWBNApplication.user.token == null) {
                    false
                } else {
                    viewModel.navigateToProfilePage()
                    true
                }
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
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalLoginFragment())
                    showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                }
                viewModel.doneNavigateToLoginPage()
            }
        })

        /** HOME PAGE */
        viewModel.shouldNavigateToHomePage.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalHomeFragment())
                    showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                }
                viewModel.doneNavigateToHomePage()
            }
        })

        /** CATEGORY SCENE */
        viewModel.shouldNavigateToCategoryPage.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalCategoryFragment())
                    showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                }
                viewModel.doneNavigateToCategory()
            }
        })

        /** SEARCH SCENE */
        viewModel.shouldNavigateToSearchPopular.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalSearchFragment(SearchFilters.POPULARITY.value))
                    binding.bottomnavMain.selectedItemId = R.id.navigation_search
                }
                viewModel.doneNavigateToSearchPopular()
            }
        })

        viewModel.shouldNavigateToSearchRecommend.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalSearchFragment(SearchFilters.RECOMMENDED.value))
                    binding.bottomnavMain.selectedItemId = R.id.navigation_search
                }
                viewModel.doneNavigateToSearchRecommend()
            }
        })

        /** PROFILE SCENE */
        viewModel.shouldNavigateToProfilePage.observe(this, Observer {
            it?.let {

                performNavigation {
                    if (IWBNApplication.user.token == null) {
                        viewModel.doneNavigateToProfilePage()
                        viewModel.navigateToLoginPage()
                    } else {
                        binding.bottomnavMain.selectedItemId = R.id.navigation_profile
                        nav.navigate(NavigationDirections.actionGlobalProfileFragment(it))
                        showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                    }
                }
                viewModel.doneNavigateToProfilePage()
            }
        })

        /** BOOK SCENE */
//        viewModel.selectedBook.observe(this, Observer {
//            it?.let {book ->
//                nav.navigate(NavigationDirections.actionGlobalBookDetailReaderFragment(book))
//                showBars(ToolBarBottomNavDisplays.HIDEBOTH)
//                viewModel.doneNavigateToBook()
//            }
//        })

        /** BOOK EDITING SCENE */
        viewModel.selectBookToEdit.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalBookWriteDetailFragment(it))
                    showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                }
                viewModel.doneDisplayingEditingBook()
            }
        })

        /** EDITOR SCENE -> EDITOR TEXT FRAGMENT */
        viewModel.selectedChapterForEditing.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalEditorFragment(it))
//                    nav.navigate(NavigationDirections.actionGlobalScrollableEditorFragment(it))
                    showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                }
                viewModel.doneNavigateToEditor()
            }
        })

        /** BOOK READING FRAGMENT */
        viewModel.selectedBookToRead.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalBookDetailReaderFragment(it))
                    showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                }
                viewModel.doneDisplayingReadingBook()
            }
        })

        /** CHAPTER READING FRAGMENT */
        viewModel.selectedChapterToRead.observe(this, Observer {
            it?.let {
                //TODO: CONNECT to READER SCENE
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalReaderMixerFragment(it.first, it.second))
                    showBars(ToolBarBottomNavDisplays.HIDEBOTH)
                }
                viewModel.doneSelectChapterToRead()
            }
        })

        viewModel.selectedBookToManage.observe(this, Observer {
            it?.let {
                performNavigation {
                    nav.navigate(NavigationDirections.actionGlobalBookDetailManageFragment(it))
                }
                viewModel.doneSelectBookToManage()
            }
        })

        currentFragment = R.id.loginFragment

        // NAVIGATION
        binding.bottomnavMain.setOnNavigationItemSelectedListener(bottomNavOnItemSelectedListener)

        viewModel.currentFragmentType.observe(this, Observer {
            it?.let {
                when(it) {
                    CurrentFragmentType.PROFILE, CurrentFragmentType.HOME -> {
                        showBars(ToolBarBottomNavDisplays.DISPLAYBOTTOMNAVONLY)
                    }
                }
            }
        })

        setupOnNavigateListener()
        setupDialogObservers()
    }

    private fun setupOnNavigateListener() {
        nav.addOnDestinationChangedListener { controller, _, _ ->
            viewModel.currentFragmentType.value =  when(controller.currentDestination?.id) {
                R.id.loginFragment -> CurrentFragmentType.LOGIN
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.categoryFragment -> CurrentFragmentType.CATEGORY
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.bookWriteDetailFragment -> CurrentFragmentType.BOOKDETAIL
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupDialogObservers() {
        viewModel.dialogInfo.observe(this, Observer {
            it?.let {
                displayLoadingDialog(it.first, it.second)
                viewModel.doneReceivingDialogInfo()
            }
        })
    }

    private fun performNavigation(action:()->Unit) {
        if (loadingDialog != null && loadingDialog!!.isVisible) {
            Handler().postDelayed({
                viewModel.doneReceivingDialogInfo()
            }, TimeConstants.TIME_1000_ms)
            Handler().postDelayed({
                action()
            }, TimeConstants.TIME_500_ms)
        } else {
            action()
        }
        viewModel.doneDisplayingSimpleTextScene()
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

    fun showAPIStatusDialog(status: ApiLoadingStatus) {
        when(status) {
            ApiLoadingStatus.LOADING -> {

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

    /** DIALOG */
    private fun displayLoadingDialog(message: String, status: ApiLoadingStatus) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }

        loadingDialog?.setMessage(message, status)

        if (!loadingDialog!!.isVisible) {
            loadingDialog?.show(supportFragmentManager, "loadingDialog")
        }

        if (status == ApiLoadingStatus.DONE) {

            var mills = TimeConstants.TIME_1000_ms

            if (message.isEmpty()) {
                mills = TimeConstants.TIME_INSTANCE
            }

            Handler().postDelayed({
                loadingDialog?.dismiss()
                loadingDialog = null
            }, mills)
        }

        if (status == ApiLoadingStatus.ERROR) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }

    }

}
