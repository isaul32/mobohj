package li.sau.projectwork

//import android.text.Html
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.DefaultTagHandler
import li.sau.projectwork.utils.html.Html
import li.sau.projectwork.workers.blog.PostWorker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)

        WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
                .observe(this, Observer { status ->
                    if (status != null && status.state.isFinished) {
                        renderBlog()
                    }
                })
    }

    fun renderBlog() {
        // Test blog view
        val database = AppDatabase.getInstance(applicationContext)
        database.blogPostDao().getAll()
                .observe(this, Observer { posts ->
                    val post = posts[2]
                    val title = post.title.rendered

                    val htmlToSpanned = DefaultTagHandler(this, html_view, BASE_URI)
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
                })
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
