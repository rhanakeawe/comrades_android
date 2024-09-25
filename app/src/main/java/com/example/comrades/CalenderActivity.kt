package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CalenderActivity : ComponentActivity() {

    private lateinit var calendar: CalendarView
    private lateinit var dateText: TextView
    private lateinit var setButton: Button
    private lateinit var spinner: Spinner
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userText: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        calendar = findViewById(R.id.calendarView)
        dateText = findViewById(R.id.calendertext)
        setButton = findViewById(R.id.setButton)
        spinner = findViewById(R.id.spinner)
        userText = findViewById(R.id.userText)
        logoutButton = findViewById(R.id.logoutButton)

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
            addEvent(dateText.text.toString(), spinner.selectedItem.toString())
        }
    }

    private fun addEvent(date:String, type:String) {
        // TODO: Add Event To The Firebase Data
        Toast.makeText(this, "$type on $date Added!",
            Toast.LENGTH_LONG).show()
    }
}