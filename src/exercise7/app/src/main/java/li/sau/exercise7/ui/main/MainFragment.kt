package li.sau.exercise7.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import li.sau.exercise7.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.main_fragment.*
import li.sau.exercise7.Book
import li.sau.exercise7.BookListAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestoreSettings






class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        database = FirebaseFirestore.getInstance()
        database.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()


        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let { ctx ->

            // Create adapter
            val adapter = BookListAdapter(ctx)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(this.context)

            // Initialize data structure, if user is authenticated
            auth.currentUser?.let { user ->
                // Creating the document if it does not already exist
                database.collection("users")
                        .document(user.uid)
                        .set(emptyMap(), SetOptions.merge())
                        .addOnSuccessListener {
                            database.collection("users")
                                    .document(user.uid)
                                    .collection("books")
                                    .addSnapshotListener { result, _ ->
                                        result?.let { snapshot ->
                                            val books: List<Book> = snapshot.toObjects(Book::class.java)

                                            books.onEach { book ->
                                                adapter.addBook(book)
                                            }
                                        }
                                    }
                        }

            }

            activity?.fab?.setOnClickListener {
                val dialog = AddBookDialogFragment()
                dialog.setTargetFragment(this, 1)
                dialog.show(fragmentManager, "addBookDialog")

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            if (data.extras.containsKey("name")) {

                // Create book
                val book = Book(name = data.extras.getString("name"))

                // Put ISBN
                if (data.extras.containsKey("isbn")) {
                    book.isbn = data.extras.getString("isbn")
                }

                // Put year of publication
                if (data.extras.containsKey("year")) {
                    try {
                        book.yearOfPublication = data.extras.getString("year").toInt()
                    } catch (ex: NumberFormatException) { }
                }

                // Put date of Acquisition
                if (data.extras.containsKey("date")) {
                    try {
                        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        try {
                            book.dateOfAcquisition = format.parse(data.extras.getString("date"))
                        } catch (ex: ParseException) { }

                    } catch (ex: IllegalArgumentException) { }
                }

                // Save book
                auth.currentUser?.let {
                    database.collection("users")
                            .document(it.uid)
                            .collection("books")
                            .add(book)
                }
            }
        }

    }
}