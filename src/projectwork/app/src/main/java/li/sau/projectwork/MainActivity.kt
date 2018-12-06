package li.sau.projectwork

import android.os.Bundle
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.DefaultTagHandler
import li.sau.projectwork.utils.html.Html
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavController = Navigation.findNavController(this, R.id.my_nav_host_fragment)

        setSupportActionBar(toolbar)

        setupActionBar(mNavController)
        setupNavigationMenu(mNavController)

        nav.setNavigationItemSelectedListener(this)

        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        /*
        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)

        WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
                .observe(this, Observer { status ->
                    if (status != null && status.state.isFinished) {
                        renderBlog()
                    }
                })
        */
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
    */

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId


        when (id) {
            R.id.nav_blogs -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.blogsFragment)
            R.id.nav_videos -> Unit
            R.id.nav_pictures -> Unit
            R.id.nav_contact_us -> Unit
            R.id.nav_settings -> Unit
            R.id.nav_about -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.aboutFragment)
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return NavigationUI.navigateUp(findNavController(R.id.my_nav_host_fragment), drawer)
    }

    private fun renderBlog() {
        // Test blog view
        val database = AppDatabase.getInstance(applicationContext)
        database.blogPostDao().getAll()
                .observe(this, Observer { posts ->
                    val post = posts[6]
                    val title = post.title.rendered

                    val htmlToSpanned = DefaultTagHandler(this, html_view,
                            BASE_URI,
                            ResourcesCompat.getFont(this, R.font.lato),
                            ResourcesCompat.getFont(this, R.font.aleo))
                    val sb = StringBuilder()

                    // Add title
                    sb.append("<h1>$title</h1>")
                    sb.append("\n")

                    // Try add date
                    try {

                        val dateFormatGmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                        dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
                        val date = dateFormatGmt.parse(post.date_gmt)

                        // Use system locale
                        val formattedDate = DateFormat.getDateFormat(this).format(date)
                        sb.append("<h6>$formattedDate</h6>")
                        sb.append("\n")

                    } catch (e: ParseException) {
                        Log.w(TAG, "Could not parse date: " + post.date)
                    }

                    // Add excerpt
                    val excerpt = post.excerpt.rendered
                    sb.append(excerpt)

                    // add content
                    sb.append("\n")
                    val content = post.content.rendered
                    sb.append(content)

                    html_view.text = Html.fromHtml(sb.toString(), htmlToSpanned)
                    html_view.movementMethod = LinkMovementMethod()
                })
    }

    private fun setupNavigationMenu(navController: NavController) {
        // In split screen mode, you can drag this view out from the left
        // This does NOT modify the actionbar
        nav.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController) {
        // This allows NavigationUI to decide what label to show in the action bar
        // And, since we are passing in drawerLayout, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, drawer)
    }
}
