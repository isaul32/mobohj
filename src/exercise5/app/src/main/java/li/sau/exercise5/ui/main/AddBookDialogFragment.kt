package li.sau.exercise5.ui.main

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import li.sau.exercise5.R

class AddBookDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_book_dialog, container, false)

        // Todo: onclick handelrs

        return view
    }
}