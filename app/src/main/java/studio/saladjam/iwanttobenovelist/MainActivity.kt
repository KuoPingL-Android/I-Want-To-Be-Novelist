package studio.saladjam.iwanttobenovelist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import studio.saladjam.iwanttobenovelist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Int = -1

    private val bottomNavOnItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        val nav = findNavController(R.id.fragment_main)

        when(item.itemId) {
            R.id.navigation_home -> {
                nav.navigate(NavigationDirections.actionGlobalHomeFragment())
                true
            }

            R.id.navigation_category -> {
                nav.navigate(NavigationDirections.actionGlobalCategoryFragment())
                true
            }

            R.id.navigation_profile -> {
                nav.navigate(NavigationDirections.actionGlobalProfileFragment())
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


        currentFragment = R.layout.fragment_login



//        // NAVIGATION
//        binding.bottomnavMain.setOnNavigationItemSelectedListener(bottomNavOnItemSelectedListener)

    }

    override fun onBackPressed() {



        super.onBackPressed()
    }
}
