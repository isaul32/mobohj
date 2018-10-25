package li.sau.exercise7.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import li.sau.exercise7.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
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
                                        result?.let { snapshots ->
                                            snapshots.documentChanges.forEach { dc ->
                                                val book = dc.document.toObject(Book::class.java)
                                                book.id = dc.document.id
                                                when (dc.type) {
                                                    DocumentChange.Type.ADDED -> {
                                                        adapter.addBook(book)
                                                    }
                                                    DocumentChange.Type.REMOVED -> {
                                                        adapter.removeBook(book)
                                                    }
                                                    DocumentChange.Type.MODIFIED -> {
                                                        adapter.removeBook(book)
                                                        adapter.addBook(book)
                                                    }
                                                }
                                            }
                                        }
                                    }
                        }

            }

            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                private val deleteIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_delete_white_24dp)
                private val intrinsicWidth = deleteIcon!!.intrinsicWidth
                private val intrinsicHeight = deleteIcon!!.intrinsicHeight
                private val background = ColorDrawable()
                private val backgroundColor = Color.parseColor("#f44336")
                private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val book = adapter.getBook(position)

                    // Remove from view
                    adapter.removeBook(position)

                    // Remove from database if has id
                    book.id?.let { id ->
                        auth.currentUser?.let { user ->
                            database.collection("users")
                                    .document(user.uid)
                                    .collection("books")
                                    .document(id)
                                    .delete()
                        }
                    }
                }

                override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    val itemView = viewHolder!!.itemView
                    val itemHeight = itemView.bottom - itemView.top
                    val isCanceled = dX == 0f && !isCurrentlyActive

                    if (isCanceled) {
                        c?.drawRect(itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), clearPaint)
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        return
                    }

                    // Draw background
                    background.color = backgroundColor
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    background.draw(c)

                    // Calculate position of delete icon
                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    // Draw icon
                    deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon.draw(c)

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }

                // Remove highlight shadow
                override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    return
                }
            }

            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerview)

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
                    } catch (ex: NumberFormatException) {
                    }
                }

                // Put date of Acquisition
                if (data.extras.containsKey("date")) {
                    try {
                        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        try {
                            book.dateOfAcquisition = format.parse(data.extras.getString("date"))
                        } catch (ex: ParseException) {
                        }

                    } catch (ex: IllegalArgumentException) {
                    }
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