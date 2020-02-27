package com.example.app_kotlin_messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_register.setOnClickListener {
            performRegister()
        }


        already_have_account.setOnClickListener {
            Log.i("Main Activity", "Already have account")
            val logInIntent = Intent(this, LogInActivity::class.java)
            startActivity(logInIntent)
        }
    }

    private fun performRegister() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(baseContext, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        "Main Activity",
                        "Created user with email and password: success uid: ${it.result!!.user!!.uid}"
                    )
                    Toast.makeText(baseContext, "Created user success", Toast.LENGTH_SHORT).show()
                    val user = it.result!!.user
                    return@addOnCompleteListener
                }
            }.addOnFailureListener {
                Log.w("Main Activity", "Created user with email and password: failed")
                Toast.makeText(baseContext, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }

    }
}
