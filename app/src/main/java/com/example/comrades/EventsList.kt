package com.example.comrades

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.cardview.widget.CardView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EventsList : Fragment(R.layout.fragment_events_list) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val db = Firebase.firestore
        val eventsRef = db.collection("events")

        val view = inflater.inflate(R.layout.fragment_events_list, container, false)

        val layout : LinearLayout = view.findViewById(R.id.linearLayout)

        val query = eventsRef.orderBy("eventDate")

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var list = ""
                    Log.d(TAG, "${document.id} => ${document.data}")
                    list += "Name: " + document.data["eventName"].toString() + "\n"
                    list += "Date: " + document.data["eventDate"].toString() + "\n"
                    list += "Type: " + document.data["eventType"].toString() + "\n"
                    list += "User: " + document.data["eventUser"].toString() + "\n\n"
                    Log.d(TAG, list)

                    val cardView = CardView(requireContext())
                    val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(20,40,20,40)
                    cardView.layoutParams = layoutParams
                    cardView.setContentPadding(70,50,70,50)
                    cardView.radius = 20F
                    cardView.elevation = 50F

                    val cardRelativeLayout = RelativeLayout(requireContext())

                    val eventsListText = TextView(requireContext())
                    eventsListText.textSize = 20F
                    eventsListText.text = list
                    eventsListText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    eventsListText.gravity = Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL

                    cardRelativeLayout.addView(eventsListText)
                    cardView.addView(cardRelativeLayout)
                    layout.addView(cardView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        return view
    }

}