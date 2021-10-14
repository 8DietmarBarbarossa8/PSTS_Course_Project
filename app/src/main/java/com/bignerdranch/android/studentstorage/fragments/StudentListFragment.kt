package com.bignerdranch.android.studentstorage.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.studentstorage.Callbacks
import com.bignerdranch.android.studentstorage.R
import com.bignerdranch.android.studentstorage.model.Student
import com.bignerdranch.android.studentstorage.viewmodel.StudentListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentListFragment : Fragment() {
    private lateinit var studentRecyclerView: RecyclerView
    private var adapter: StudentAdapter = StudentAdapter(emptyList())
    private val studentListViewModel: StudentListViewModel by lazy {
        ViewModelProviders.of(this).get(StudentListViewModel::class.java)
    }
    private var callbacks: Callbacks? = null
    private lateinit var menu: Menu
    private var addingButton: FloatingActionButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
        studentListViewModel.sortStudents(requireNotNull(arguments?.getString(SORTING_MODE)))
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

        studentListViewModel.studentListLiveData.observe(
            viewLifecycleOwner,
            { students ->
                students?.let {
                    adapter.students = students
                    studentRecyclerView.adapter = adapter
                }
            }
        )

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
        private val creationDateTextView: TextView = itemView.findViewById(R.id.creation_date_text)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(student: Student) {
            this.student = student
            nameTextView.text = this.student.name
            ageTextView.text = this.student.age.toString()
            ratingTextView.text = this.student.rating.toString()
            creationDateTextView.text = this.student.date.toString()
        }

        override fun onClick(v: View) {
            callbacks?.onStudentSelected(student.id)
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