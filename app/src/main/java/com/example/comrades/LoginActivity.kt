package com.example.comrades

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : ComponentActivity() {

    private lateinit var enterButton: Button
    private lateinit var emailBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var registerText: TextView

    private lateinit var mAuth : FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser : FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, CalenderActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }
    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val emailInput = emailBox.text.toString()
            val passwordInput = passwordBox.text.toString()
            enterButton.isEnabled = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun loginSuccess() {
        startActivity(Intent(this, CalenderActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginmenu)
        enableEdgeToEdge()

        mAuth = FirebaseAuth.getInstance()

        enterButton = findViewById(R.id.enterbutton)
        emailBox = findViewById(R.id.email_box)
        passwordBox = findViewById(R.id.password_box)
        registerText = findViewById(R.id.registerhere)

        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        emailBox.addTextChangedListener(textWatcher)
        passwordBox.addTextChangedListener(textWatcher)

        enterButton.setOnClickListener {
            val email = emailBox.text.toString()
            val password = emailBox.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCustomToken:success")
                        loginSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
}