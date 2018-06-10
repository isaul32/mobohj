package li.sau.exercise5.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import li.sau.exercise5.Book
import li.sau.exercise5.BookListAdapter
import li.sau.exercise5.R
import java.util.*

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
            //Log.d("MainFragment", "Insert isn't implemented")
            val book = Book(name = "Testi")
            book.isbn = "978-1-56619-909-4"
            book.yearOfPublication = 2018
            book.dateOfAcquisition = Date()
            viewModel.insert(book)
        }
    }

}

private fun <T> LiveData<T>.observe(mainFragment: MainFragment, observer: Observer<T>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
