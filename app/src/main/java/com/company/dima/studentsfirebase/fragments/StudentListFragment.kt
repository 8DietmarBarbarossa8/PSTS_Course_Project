package com.company.dima.studentsfirebase.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.company.dima.studentsfirebase.inter_face.Callbacks
import com.company.dima.studentsfirebase.R
import com.company.dima.studentsfirebase.model.Student
import com.company.dima.studentsfirebase.database.StudentFirebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class StudentListFragment : Fragment() {
    private lateinit var studentRecyclerView: RecyclerView
    private var adapter: StudentAdapter = StudentAdapter(emptyList())
    private var callbacks: Callbacks? = null
    private lateinit var menu: Menu
    private var addingButton: FloatingActionButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
        StudentFirebase.students.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        studentRecyclerView = view.findViewById(R.id.student_recycler_view) as RecyclerView
        studentRecyclerView.layoutManager = LinearLayoutManager(context)
        studentRecyclerView.adapter = adapter
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_student_list, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sorting_student -> {
                callbacks?.onSettings()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            callbacks?.onMainScreen()
        }

        StudentFirebase.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children)
                    try {
                        StudentFirebase
                            .students.add(requireNotNull(ds.getValue(Student::class.java)))
                    } catch (e: Exception) {
                        continue
                    }

                val set = HashSet<Student>(StudentFirebase.students)
                StudentFirebase.students.clear()
                StudentFirebase.students.addAll(set)
                StudentFirebase.sortStudents(requireNotNull(arguments?.getString(SORTING_MODE)))
                adapter.students = StudentFirebase.students
                studentRecyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "$databaseError", Toast.LENGTH_SHORT).show()
            }
        })

        addingButton = view.findViewById(R.id.floatingActionButton)
        addingButton?.setOnClickListener {
            callbacks?.onCreateNewStudent()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class StudentHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var student: Student
        private val nameTextView: TextView = itemView.findViewById(R.id.name_student)
        private val ageTextView: TextView = itemView.findViewById(R.id.age_student)
        private val ratingTextView: TextView = itemView.findViewById(R.id.rating_student)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(student: Student) {
            this.student = student
            nameTextView.text = this.student.name
            ageTextView.text = this.student.age.toString()
            ratingTextView.text = this.student.rating.toString()
        }

        override fun onClick(v: View) {
            callbacks?.onStudentSelected(student.pathKey)
        }
    }

    private inner class StudentAdapter(var students: List<Student>) :
        RecyclerView.Adapter<StudentHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_student, parent, false)
            return StudentHolder(view)
        }

        override fun onBindViewHolder(holder: StudentHolder, position: Int) {
            holder.bind(students[position])
        }

        override fun getItemCount() = students.size
    }

    companion object {
        private const val SORTING_MODE = "sorting mode"

        @JvmStatic
        fun newInstance(sortingMode: String = ""):
                StudentListFragment = StudentListFragment().apply {
            arguments = bundleOf(SORTING_MODE to sortingMode)
        }
    }
}