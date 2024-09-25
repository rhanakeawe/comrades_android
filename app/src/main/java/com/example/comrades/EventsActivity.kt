package com.example.comrades

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

class EventsActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val backButton = Button(this)

        backButton.setOnClickListener() {
            startActivity(
                Intent(this, CalenderActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        layout.addView(backButton)
        setContentView(layout)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val db = Firebase.firestore
        val eventsRef = db.collection("events")

        val query = eventsRef.orderBy("eventDate")

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var list = ""
                    Log.d(TAG, "${document.id} => ${document.data}")
                    list += "Name: " + document.data.get("eventName").toString() + "\n"
                    list += "Date: " + document.data.get("eventDate").toString() + "\n"
                    list += "Type: " + document.data.get("eventType").toString() + "\n"
                    list += "User: " + document.data.get("eventUser").toString() + "\n\n"
                    Log.d(TAG, list)
                    val textView = TextView(this)
                    textView.textSize = 20F
                    textView.text = list
                    layout.addView(textView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}