package com.example.app_kotlin_messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LogInActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        back_to_register.setOnClickListener {
            startActivityRegister()
        }

        button_login.setOnClickListener {
            performLogIn()
            return@setOnClickListener
        }
    }

    private fun  performLogIn() {
        val email = email_login.text.toString()
        val password = password_login.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(baseContext, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LogIn Activity", "Sign In success: ${it.result!!.user!!.uid}")
                    Toast.makeText(baseContext, "Sign In successfully!", Toast.LENGTH_SHORT).show()
                    startActivityLatestMessageActivity()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                Log.w("LogIn Activity", "Sign In failed: ${it.message}")
                Toast.makeText(baseContext, "Sign In failed!", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }

    private fun startActivityLatestMessageActivity() {
        val intent = Intent(this, LatestMessageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun startActivityRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}