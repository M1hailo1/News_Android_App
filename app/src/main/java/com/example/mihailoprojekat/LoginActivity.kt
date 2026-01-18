package com.example.mihailoprojekat

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var registerNow: TextView
    lateinit var loginBtn: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailText)
        passwordInput = findViewById(R.id.passwordText)
        loginBtn = findViewById(R.id.loginBtn)
        registerNow = findViewById(R.id.registerNow)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            goToMainActivity()
        }

        registerNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        goToMainActivity()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}