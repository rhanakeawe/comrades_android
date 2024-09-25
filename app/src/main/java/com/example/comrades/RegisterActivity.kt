package com.example.comrades

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : ComponentActivity() {
    private lateinit var enterButton: Button
    private lateinit var emailBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var passwordBoxConfirm : EditText

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

    private fun registerSuccess() {
        startActivity(Intent(this, CalenderActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        enableEdgeToEdge()

        mAuth = FirebaseAuth.getInstance()

        enterButton = findViewById(R.id.enterbutton)
        emailBox = findViewById(R.id.email_box)
        passwordBox = findViewById(R.id.password_box)
        passwordBoxConfirm = findViewById(R.id.password_box_confirm)

        emailBox.addTextChangedListener(textWatcher)
        passwordBox.addTextChangedListener(textWatcher)

        enterButton.setOnClickListener() {
            val email = emailBox.text.toString()
            val password = emailBox.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Created",
                            Toast.LENGTH_SHORT).show()
                        registerSuccess()
                    } else {
                        Toast.makeText(this, "Authentification failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}