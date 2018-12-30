package li.sau.projectwork.ui.fragments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import li.sau.projectwork.R

class SettingsFragment : PreferenceFragmentCompat() {

    var mShowSeppoCount: Int = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val version = findPreference("version")
        preferenceScreen.widgetLayoutResource = R.layout.seppo
        version.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            // Need 3 click
            if (mShowSeppoCount < 2) {
                mShowSeppoCount++
            } else {
                mShowSeppoCount = 0
                val action = SettingsFragmentDirections.actionSettingsFragmentToSeppoFragment()
                findNavController().navigate(action)
            }
            true
        }

        version.summary = context
                ?.packageManager
                ?.getPackageInfo(context?.packageName, 0)
                ?.versionName

        // Sync is always on. This means that post are saved in db.
        val sync = findPreference("enable_sync") as SwitchPreferenceCompat
        sync.isChecked = true
        sync.isEnabled = false

    }

}
