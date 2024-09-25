package com.example.comrades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

class CalenderActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java,
            "events.db"
        ).build()
    }

    private val viewModel by viewModels<EventViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventViewModel(db.dao) as T
                }
            }
        }
    )

    private lateinit var calendar: CalendarView
    private lateinit var dateText: TextView
    private lateinit var setButton: Button
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)
        enableEdgeToEdge()

        calendar = findViewById(R.id.calendarView)
        dateText = findViewById(R.id.calendertext)
        setButton = findViewById(R.id.setButton)
        spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this, R.array.type_array, android.R.layout.simple_spinner_item).also {
                arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = arrayAdapter
        }

        calendar.setOnDateChangeListener() {
            _,year,month,day ->
            val date = ("%02d".format(month+1) + "/" + "%02d".format(day) + "/" + year)
            dateText.setText(date.toString())
        }

        setButton.setOnClickListener() {
            addEvent(dateText.text.toString(), spinner.selectedItem.toString())
        }
    }

    private fun addEvent(date:String, type:String) {
        // TODO: Add Event To The Firebase Data
        Toast.makeText(this, "$type on $date Added!",
            Toast.LENGTH_LONG).show();
    }
}