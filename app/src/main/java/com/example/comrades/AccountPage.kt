package com.example.comrades

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AccountPage : Fragment(R.layout.fragment_account) {

    private lateinit var accountName : TextView
    private lateinit var desc : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        accountName = view.findViewById(R.id.accountName)
        desc = view.findViewById(R.id.desc)

        accountName.text = arguments?.getString("userName")
        desc.text = arguments?.getString("userDesc")

        return view
    }

}