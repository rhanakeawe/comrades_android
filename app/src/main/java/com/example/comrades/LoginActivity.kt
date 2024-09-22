package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class LoginActivity : ComponentActivity() {

    private lateinit var enterButton: Button
    private lateinit var emailBox: EditText
    private lateinit var passwordBox: EditText

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val emailInput = emailBox.text.toString()
            val passwordInput = passwordBox.text.toString()
            enterButton.isEnabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun checkLogin() {
        // TODO: Check login details with Firebase
        var credential : Boolean = true
        if (credential == true) {
            loginSuccess()
        }
    }

    private fun loginSuccess() {
        startActivity(Intent(this, CalenderActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginmenu)
        enableEdgeToEdge()
        enterButton = findViewById(R.id.enterbutton)
        emailBox = findViewById(R.id.email_box)
        passwordBox = findViewById(R.id.password_box)

        emailBox.addTextChangedListener(textWatcher)
        passwordBox.addTextChangedListener(textWatcher)

        enterButton.setOnClickListener() {checkLogin()}
    }
}