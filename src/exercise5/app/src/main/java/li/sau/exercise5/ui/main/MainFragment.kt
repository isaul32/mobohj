package li.sau.exercise5.ui.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import li.sau.exercise5.Book
import li.sau.exercise5.BookListAdapter
import li.sau.exercise5.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = BookListAdapter(this.context!!)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this.context)


        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllBooks().observe(this, Observer<List<Book>> {
            value -> value?.let{ adapter.setBooks(it) }
        })

        activity?.fab?.setOnClickListener {
            val dialog = AddBookDialogFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(fragmentManager, "addBookDialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.extras.containsKey("name")) {
                val book = Book(name = data.extras.getString("name"))
                if (data.extras.containsKey("isbn")) {
                    book.isbn = data.extras.getString("isbn")
                }
                if (data.extras.containsKey("year")) {
                    //book.yearOfPublication = data.extras.getString("year")
                }
                if (data.extras.containsKey("date")) {
                    //book.dateOfAcquisition = data.extras.getString("date")
                }
                viewModel.insert(book)
            }
        }

    }
}