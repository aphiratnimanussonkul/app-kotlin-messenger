package com.example.app_kotlin_messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        val adapter = GroupAdapter<GroupieViewHolder>()

        recycle_view_new_message.adapter = adapter
        recycle_view_new_message.layoutManager = LinearLayoutManager(this)

        fetchUser()
    }

    private fun fetchUser() {
        val database = FirebaseFirestore.getInstance()
        val collection = database.collection("users")
        collection.get()
            .addOnSuccessListener { document ->
                val adapter = GroupAdapter<GroupieViewHolder>()

                if (document != null) {
                    document.forEach {
                        Log.d("NewMessageActivity", it.data.toString())
                        val user = User(
                            uid = it.data.get("uid") as String,
                            username = it.data.get("username") as String,
                            imageProfile = it.data.get("imageProfile") as String
                        )
                        if (user != null) {
                            adapter.add(UserItem(user))
                        }
                    }
                    recycle_view_new_message.adapter = adapter
                } else {
                    Log.d("NewMessageActivity", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("NewMessageActivity", "get failed with ", exception)
            }

    }
}

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {}

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}