package li.sau.exercise5.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.add_book_dialog.view.*
import li.sau.exercise5.R
import java.text.SimpleDateFormat
import java.util.*


class AddBookDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_book_dialog, container, false)

        // Current date
        val today = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = formatter.format(today)
        view.date.setText(date)

        view.add.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", view.name.text.toString())
            bundle.putString("isbn", view.isbn.text.toString())
            bundle.putString("year", view.year.text.toString())
            bundle.putString("date", view.date.text.toString())
            val intent = Intent().putExtras(bundle)

            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            dismiss()
        }

        return view
    }
}