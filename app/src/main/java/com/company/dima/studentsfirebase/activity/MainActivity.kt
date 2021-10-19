package com.company.dima.studentsfirebase.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.company.dima.studentsfirebase.R
import com.company.dima.studentsfirebase.fragments.StudentFragment
import com.company.dima.studentsfirebase.fragments.StudentListFragment
import com.company.dima.studentsfirebase.fragments.StudentSettingsFragment
import com.company.dima.studentsfirebase.inter_face.Callbacks

class MainActivity : AppCompatActivity(), Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) onMainScreen()
    }

    override fun onMainScreen(sortingMode: String) {
        this.setTitle(R.string.app_name)
        startFragment(StudentListFragment.newInstance(sortingMode))
    }

    override fun onCreateNewStudent() {
        this.setTitle(R.string.adding_label)
        startFragment(StudentFragment.newInstance())
    }

    override fun onStudentSelected(pathKey: String) {
        this.setTitle(R.string.editing_label)
        startFragment(StudentFragment.newInstance(pathKey))
    }

    override fun onSettings() {
        this.setTitle(R.string.setting_label)
        startFragment(StudentSettingsFragment.newInstance())
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}