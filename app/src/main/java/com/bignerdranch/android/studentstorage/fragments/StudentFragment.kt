package com.bignerdranch.android.studentstorage.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.studentstorage.Callbacks
import com.bignerdranch.android.studentstorage.R
import com.bignerdranch.android.studentstorage.model.Student
import com.bignerdranch.android.studentstorage.viewmodel.StudentDetailViewModel
import java.util.*

class StudentFragment : Fragment() {
    private lateinit var nameStudent: EditText
    private lateinit var ageStudent: EditText
    private lateinit var ratingStudent: EditText
    private lateinit var functionalButton: Button
    private lateinit var neutralButton: Button
    private val studentDetailViewModel: StudentDetailViewModel by lazy {
        ViewModelProviders.of(this).get(StudentDetailViewModel::class.java)
    }
    private var student: Student = Student()
    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (correctStudentData())
                studentDetailViewModel.saveStudent(student)
            snapBackToReality()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student, container, false)

        nameStudent = view.findViewById(R.id.name_student) as EditText
        ageStudent = view.findViewById(R.id.age_student) as EditText
        ratingStudent = view.findViewById(R.id.rating_student) as EditText
        functionalButton = view.findViewById(R.id.add_button) as Button
        neutralButton = view.findViewById(R.id.neutral_button) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val studentId = arguments?.getSerializable(ARG_STUDENT_ID) as UUID?

        if (studentId == null) addStudent()
        else {
            functionalButton.text = resources.getString(R.string.delete)
            functionalButton.background = ResourcesCompat
                .getDrawable(resources, R.drawable.deleting_button_border, null)

            studentDetailViewModel.loadStudent(studentId)
            studentDetailViewModel.studentLiveData.observe(
                viewLifecycleOwner,
                { student ->
                    student?.let {
                        this.student = student
                        updateUI()
                    }
                }
            )

            functionalButton.setOnClickListener {
                studentDetailViewModel.deleteStudent(student)
                Toast.makeText(context, R.string.deleting_notification, Toast.LENGTH_SHORT).show()
                snapBackToReality()
            }
        }

        neutralButton.setOnClickListener {
            if (correctStudentData())
                studentDetailViewModel.saveStudent(student)
            snapBackToReality()
        }
    }

    private fun updateUI() {
        nameStudent.setText(student.name)
        ageStudent.setText(student.age.toString())
        ratingStudent.setText(student.rating.toString())
    }

    private fun addStudent() {
        functionalButton.setOnClickListener {
            val name = nameStudent.text.toString()
            val age = ageStudent.text.toString().toIntOrNull()
            val rating = ratingStudent.text.toString().toFloatOrNull()
            if (name.isNotEmpty() && age != null && rating != null) {
                student = Student(UUID.randomUUID(), name, age, rating, Date())
                studentDetailViewModel.addStudent(student)
                Toast.makeText(context, R.string.adding_notification, Toast.LENGTH_SHORT).show()
                snapBackToReality()
            } else Toast.makeText(
                context,
                R.string.input_error_notification, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun correctStudentData(): Boolean {
        val student = Student()
        student.name = nameStudent.text.toString()
        student.age = try {
            ageStudent.text.toString().toInt()
        } catch (e: Exception) {
            student.age
        }
        student.rating = try {
            ratingStudent.text.toString().toFloat()
        } catch (e: Exception) {
            student.rating
        }

        if (!(this.student.name == student.name &&
                    this.student.age == student.age &&
                    this.student.rating == student.rating)
        ) {
            this.student.name = student.name
            this.student.age = student.age
            this.student.rating = student.rating
            return true
        }

        return false
    }

    private fun snapBackToReality() {
        callbacks?.onMainScreen()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        private const val ARG_STUDENT_ID = "student_id"

        @JvmStatic
        fun newInstance(studentId: UUID): StudentFragment {
            val args = Bundle().apply { putSerializable(ARG_STUDENT_ID, studentId) }
            return StudentFragment().apply { arguments = args }
        }

        @JvmStatic
        fun newInstance() = StudentFragment()
    }
}