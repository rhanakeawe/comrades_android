package com.example.comrades

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class CalenderActivity : ComponentActivity() {

    private lateinit var calendar: CalendarView
    private lateinit var dateText: TextView
    private lateinit var setButton: Button
    private lateinit var spinner: Spinner
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userText: TextView
    private lateinit var logoutButton: Button
    private lateinit var eventNameBox: EditText
    private lateinit var eventsButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val db = Firebase.firestore

        calendar = findViewById(R.id.calendarView)
        dateText = findViewById(R.id.calendertext)
        setButton = findViewById(R.id.setButton)
        spinner = findViewById(R.id.spinner)
        userText = findViewById(R.id.userText)
        logoutButton = findViewById(R.id.logoutButton)
        eventNameBox = findViewById(R.id.eventNameBox)
        eventsButton = findViewById(R.id.eventsButton)

        eventsButton.setOnClickListener() {
            startActivity(
                Intent(this, EventsActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        eventNameBox.addTextChangedListener(textWatcher)
        dateText.addTextChangedListener(textWatcher)

        logoutButton.setOnClickListener() {
            auth.signOut()
            startActivity(
                Intent(this, LoginActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        userText.text = user.email.toString()

        ArrayAdapter.createFromResource(
            this, R.array.type_array, android.R.layout.simple_spinner_item).also {
                arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = arrayAdapter
        }

        calendar.setOnDateChangeListener() {
            _,year,month,day ->
            val date = ("%02d".format(month+1) + "/" + "%02d".format(day) + "/" + year)
            dateText.text = date
        }

        setButton.setOnClickListener() {
            addEvent(dateText.text.toString(), eventNameBox.text.toString(), spinner.selectedItem.toString(), db)
        }
    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val dateInput = dateText.text.toString()
            val eventNameInput = eventNameBox.text.toString()
            setButton.isEnabled = dateInput.isNotEmpty() && eventNameInput.isNotEmpty()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun addEvent(date:String, eventName:String, type:String, db: FirebaseFirestore) {
        Toast.makeText(this, "$type on $date Added!",
            Toast.LENGTH_LONG).show()
        val event = hashMapOf(
            "eventDate" to date,
            "eventName" to eventName,
            "eventType" to type,
            "eventUser" to user.email
        )
        db.collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}