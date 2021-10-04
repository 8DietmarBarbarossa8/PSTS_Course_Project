package com.bignerdranch.android.studentstorage.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import com.bignerdranch.android.studentstorage.Callbacks
import com.bignerdranch.android.studentstorage.R

class StudentSortingFragment : Fragment() {
    private var buttons: Array<Button?> = Array(3) { null }
    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            callbacks?.onMainScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idButtonArray = arrayOf(R.id.nameButton, R.id.ageButton, R.id.ratingButton)
        for (i in idButtonArray.indices) {
            buttons[i] = view.findViewById(idButtonArray[i])
            buttons[i]?.setOnClickListener {
                callbacks?.onMainScreen(switchSortParameter(i))
            }
        }
    }

    private fun switchSortParameter(number: Int): String =
        when (number) {
            0 -> "name"
            1 -> "age"
            2 -> "rating"
            else -> ""
        }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentSortingFragment()
    }
}