package com.example.comrades

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class CreateEvent : Fragment(R.layout.fragment_create_event) {
    private lateinit var calendar: CalendarView
    private lateinit var dateText: TextView
    private lateinit var setButton: Button
    private lateinit var spinner: Spinner
    private lateinit var user: FirebaseUser
    private lateinit var eventNameBox: EditText

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val dateInput = dateText.text.toString()
            val eventNameInput = eventNameBox.text.toString()
            setButton.isEnabled = dateInput.isNotEmpty() && eventNameInput.isNotEmpty()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        calendar = view.findViewById(R.id.calendarView)
        dateText = view.findViewById(R.id.calendertext)
        setButton = view.findViewById(R.id.setButton)
        spinner = view.findViewById(R.id.spinner)
        eventNameBox = view.findViewById(R.id.eventNameBox)

        eventNameBox.addTextChangedListener(textWatcher)
        dateText.addTextChangedListener(textWatcher)

        ArrayAdapter.createFromResource(
            requireContext(), R.array.type_array, android.R.layout.simple_spinner_item).also {
                arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = arrayAdapter
        }

        calendar.setOnDateChangeListener {
                _,year,month,day ->
            val date = ("%02d".format(month+1) + "/" + "%02d".format(day) + "/" + year)
            dateText.text = date
        }

        setButton.setOnClickListener {
            addEvent(dateText.text.toString(), eventNameBox.text.toString(), spinner.selectedItem.toString(), db)
        }
    }

    private fun addEvent(date:String, eventName:String, type:String, db: FirebaseFirestore) {
        Toast.makeText(activity, "$type on $date Added!",
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