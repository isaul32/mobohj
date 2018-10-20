package li.sau.exercise7

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import li.sau.exercise7.ui.main.MainFragment


private const val RC_SIGN_IN = 9001

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        FirebaseAuth.getInstance().currentUser?.let {
            menuInflater.inflate(R.menu.logout_menu, menu)
        } ?: run {
            menuInflater.inflate(R.menu.login_menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        return when (item?.itemId) {
            R.id.login -> {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN)
                true
            }
            R.id.logout -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            invalidateOptionsMenu()
                        }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // Show some error messages?
            // val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                // Update menu
                invalidateOptionsMenu()
            } else {
                Toast.makeText(applicationContext,
                        resources.getString(R.string.login_failed),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}
