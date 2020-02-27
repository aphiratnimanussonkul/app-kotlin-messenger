package com.example.app_kotlin_messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_register.setOnClickListener {
            val username = username_register.text.toString()
            val email = email_register.text.toString()
            val password = password_register.text.toString()

            Log.i("Main Activity", "Username: " + username + " Email: " + email + " Password: " + password)
        }

        already_have_account.setOnClickListener {
            Log.i("Main Activity", "Already have account")
        }
    }
}
