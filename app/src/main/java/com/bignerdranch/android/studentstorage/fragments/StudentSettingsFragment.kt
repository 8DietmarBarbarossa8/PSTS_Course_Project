package com.bignerdranch.android.studentstorage.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bignerdranch.android.studentstorage.Callbacks
import com.bignerdranch.android.studentstorage.R

class StudentSettingsFragment : Fragment() {
    private var buttons: Array<Button?> = Array(3) { null }
    private lateinit var contactButton: Button
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
        return inflater.inflate(R.layout.fragment_student_settings, container, false)
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

        contactButton = view.findViewById(R.id.contact_button)
        contactButton.setOnClickListener {
            if (isCallPermissionGranted()) callMe()
            else requestCallPermission()
        }
    }

    private fun switchSortParameter(number: Int): String =
        when (number) {
            0 -> "name"
            1 -> "age"
            2 -> "rating"
            else -> ""
        }

    private fun isCallPermissionGranted(): Boolean =
        context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CALL_PHONE
            )
        } == PackageManager.PERMISSION_GRANTED

    private fun requestCallPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE
        )
    }

    private fun callMe() {
        val callUri = Uri.parse("tel://${resources.getString(R.string.phone)}")
        val callIntent = Intent(Intent.ACTION_CALL, callUri)
        startActivity(callIntent)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        private const val REQUEST_CODE = 100

        @JvmStatic
        fun newInstance() = StudentSettingsFragment()
    }
}