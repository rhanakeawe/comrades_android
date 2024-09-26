package com.example.comrades

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.cardview.widget.CardView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EventsList : Fragment(R.layout.fragment_events_list) {

    private lateinit var eventsLayout : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        eventsLayout = LinearLayout(requireContext())
        eventsLayout.orientation = LinearLayout.VERTICAL
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
                    val scrollView = ScrollView(requireContext())

                    val cardView = CardView(requireContext())
                    val layoutparams = LayoutParams(1000,1000)
                    cardView.layoutParams = layoutparams

                    val cardRelativeLayout = RelativeLayout(requireContext())

                    val eventsListText = TextView(requireContext())
                    eventsListText.textSize = 20F
                    eventsListText.text = list
                    eventsListText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

                    cardRelativeLayout.addView(eventsListText)
                    cardView.addView(cardRelativeLayout)
                    scrollView.addView(cardView)
                    eventsLayout.addView(scrollView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        return eventsLayout
    }

}