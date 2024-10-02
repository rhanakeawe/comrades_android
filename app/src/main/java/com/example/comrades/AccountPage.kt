package com.example.comrades

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AccountPage : Fragment(R.layout.fragment_account) {

    private lateinit var accountName : TextView
    private lateinit var desc : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountName = view.findViewById(R.id.accountName)
        desc = view.findViewById(R.id.desc)
        val user = arguments?.getString("user")
        val db = Firebase.firestore
        val userRef = db.collection("users")

        val query = userRef.whereEqualTo("userEmail",user)

        query.get()
            .addOnSuccessListener { documents ->
                Log.i("user","Got User!")
                if (documents != null) {
                    for (document in documents) {
                        desc.text = document.data["userDesc"].toString()
                        accountName.text = document.data["userName"].toString()
                    }
                } else {
                    Log.w("user", "User null!")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("user", "Error getting user!", exception)
            }
    }

}