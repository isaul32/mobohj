package li.sau.exercise7.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import li.sau.exercise7.R
import com.google.firebase.auth.FirebaseAuth


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    //private lateinit var viewModel: MainViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val currentUser = mAuth.currentUser

        /*val adapter = BookListAdapter(this.context!!)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this.context)*/


        /*val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                val book = adapter.getBook(viewHolder.adapterPosition)
                if (book != null) {
                    viewModel.remove(book)
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

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerview)*/


        /*viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllBooks().observe(this, Observer<List<Book>> {
            value -> value?.let{ adapter.setBooks(it) }
        })*/

        activity?.fab?.setOnClickListener {
            val dialog = AddBookDialogFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(fragmentManager, "addBookDialog")

        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.extras.containsKey("name")) {
                val book = Book(name = data.extras.getString("name"))
                if (data.extras.containsKey("isbn")) {
                    book.isbn = data.extras.getString("isbn")
                }
                if (data.extras.containsKey("year")) {
                    try {
                        book.yearOfPublication = data.extras.getString("year").toInt()
                    } catch (ex: NumberFormatException) {

                    }
                }
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
                viewModel.insert(book)
            }
        }

    }*/
}