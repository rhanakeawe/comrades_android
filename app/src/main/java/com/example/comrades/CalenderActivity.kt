package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

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

        bottomNavigationView = findViewById(R.id.bottomNavView)
        fragmentContainerView = findViewById(R.id.frag_containter)

        val createEventFragment = CreateEvent()
        val eventsFragment = EventsList()
        val accountFragment = AccountPage()

        val db = Firebase.firestore
        val userRef = db.collection("users")
        val query = userRef.whereEqualTo("userEmail", auth.currentUser?.email)

        val bundle = Bundle()
        bundle.putString("userEmail","${auth.currentUser?.email}")

        query.get()
            .addOnSuccessListener { documents ->
                Log.i("user","Got User!")
                if (documents != null) {
                    for (document in documents) {
                        bundle.putString("userDesc",document.data["userDesc"].toString())
                        bundle.putString("userName", document.data["userName"].toString())
                    }
                } else {
                    Log.w("user", "User null!")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("user", "Error getting user!", exception)
            }

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