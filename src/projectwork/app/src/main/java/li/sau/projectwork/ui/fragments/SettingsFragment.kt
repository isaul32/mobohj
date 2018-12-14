package li.sau.projectwork.ui.fragments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import li.sau.projectwork.R

class SettingsFragment : PreferenceFragmentCompat() {

    var mShowSeppoCount: Int = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val preference = findPreference("version")
        preferenceScreen.widgetLayoutResource = R.layout.seppo
        preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
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
    }

}
