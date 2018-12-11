package li.sau.projectwork.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import li.sau.projectwork.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
