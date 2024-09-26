package com.example.comrades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalenderActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainerView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        enableEdgeToEdge()

        bottomNavigationView = findViewById(R.id.bottomNavView)
        fragmentContainerView = findViewById(R.id.frag_containter)

        val createEventFragment = CreateEvent()
        val eventsFragment = EventsList()
        val accountFragment = AccountPage()

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.eventsItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_containter, eventsFragment)
                        .commit()
                    true
                }
                R.id.createItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_containter, createEventFragment)
                        .commit()
                    true
                }
                R.id.accountItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_containter, accountFragment)
                        .commit()
                    true
                }
                else -> {false}
            }
        }

    }
}