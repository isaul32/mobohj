package li.sau.projectwork

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import li.sau.projectwork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)
        mDrawerLayout = binding.drawerLayout

        // Set up navigation controller
        mNavController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        val topLevelDestinations = setOf(R.id.blogsFragment,
                R.id.settingsFragment,
                R.id.aboutFragment)
        mAppBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
                .setDrawerLayout(drawerLayout)
                .build()


        // Set up action bar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(mNavController, mAppBarConfiguration)

        // Set up navigation menu
        binding.navigationView.setupWithNavController(mNavController)

        // Set up settings

        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
