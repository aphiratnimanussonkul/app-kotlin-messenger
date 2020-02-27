package com.example.app_kotlin_messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var selectPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_register.setOnClickListener {
            performRegister()
        }


        already_have_account.setOnClickListener {
            Log.d("Main Activity", "Already have account")
            startActivityLogin()
        }

        button_select_photo.setOnClickListener {
            Log.d("Main Activity", "Try to show photo selector")
            val intentSelectPhoto = Intent(Intent.ACTION_PICK)
            intentSelectPhoto.type = "image/*"
            startActivityForResult(intentSelectPhoto, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            selectPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            button_select_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()
        val confirmPassword = confirm_password_register.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(baseContext, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(baseContext, "Please enter confirm password", Toast.LENGTH_SHORT).show()
            return
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(baseContext, "Password mismatch", Toast.LENGTH_SHORT).show()
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
                    startActivityLogin()
                    return@addOnCompleteListener
                }
            }.addOnFailureListener {
                Log.w("Main Activity", "Created user with email and password: failed")
                Toast.makeText(baseContext, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }

    }

    private fun startActivityLogin() {
        val logInIntent = Intent(this, LogInActivity::class.java)
        startActivity(logInIntent)
    }
}
