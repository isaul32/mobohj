package li.sau.projectwork

import android.text.Spanned
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class TextViewModel(
        val value: ObservableField<Spanned> = ObservableField(),
        val loading: ObservableBoolean = ObservableBoolean()
)