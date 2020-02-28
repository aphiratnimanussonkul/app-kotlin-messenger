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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.log


class RegisterActivity : AppCompatActivity() {

    private var selectPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_register.setOnClickListener {
            performRegister()
        }


        already_have_account.setOnClickListener {
            Log.d("RegisterActivity", "Already have account")
            startActivityLogin()
        }

        button_select_photo.setOnClickListener {
            Log.d("RegisterActivity", "Try to show photo selector")
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
            profile_image_register.setImageBitmap(bitmap)
            button_select_photo.alpha = 0f

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
        if (selectPhotoUri == null) {
            Toast.makeText(baseContext, "Please select your image profile", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        "RegisterActivity",
                        "Created user with email and password: success uid: ${it.result!!.user!!.uid}"
                    )
                    Toast.makeText(baseContext, "Created user success", Toast.LENGTH_SHORT).show()
                    uploadImageToFirebaseStorage()
                    startActivityLogin()
                    return@addOnCompleteListener
                }
            }.addOnFailureListener {
                Log.w("RegisterActivity", "Created user with email and password: failed")
                Toast.makeText(baseContext, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }

    }

    private fun startActivityLogin() {
        val logInIntent = Intent(this, LogInActivity::class.java)
        startActivity(logInIntent)
    }

    private fun uploadImageToFirebaseStorage() {
        val fileName = UUID.randomUUID().toString()

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val folderRef = storageRef.child("/images-profile")
        val imageRef = folderRef.child(fileName)


        imageRef.putFile(selectPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Upload profile image success: ${it.metadata!!.path}")
                imageRef.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.w("RegisterActivity", "Upload profile image failed: ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(imageProfile: String) {
        val uid = FirebaseAuth.getInstance().uid
        val database = FirebaseFirestore.getInstance()
        val collection = database.collection("users")
        val user = User(username = username_register.text.toString(), uid = uid!!, imageProfile = imageProfile)
        collection.add(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Save user success: ${it.id}")
            }
            .addOnFailureListener {
                Log.w("RegisterActivity", "Save user failed ${it.message}")
            }
    }
}

class User(val username: String, val uid: String, val imageProfile: String)
