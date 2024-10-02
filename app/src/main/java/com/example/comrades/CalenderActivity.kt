package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CalenderActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentContainerView: FragmentContainerView
    private lateinit var auth : FirebaseAuth

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        enableEdgeToEdge()

        auth = Firebase.auth
        val user = auth.currentUser?.email

        bottomNavigationView = findViewById(R.id.bottomNavView)
        fragmentContainerView = findViewById(R.id.frag_containter)

        val createEventFragment = CreateEvent()
        val eventsFragment = EventsList()
        val accountFragment = AccountPage()

        val bundle = Bundle()
        bundle.putString("user","$user")

        createEventFragment.arguments = bundle
        eventsFragment.arguments = bundle
        accountFragment.arguments = bundle

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