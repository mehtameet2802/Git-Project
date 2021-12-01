package com.example.ugp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    val mAuth = Firebase.auth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val name= findViewById<EditText>(R.id.userText)
        val email = findViewById<EditText>(R.id.emailText)
        val num = findViewById<EditText>(R.id.numText)
        val image = findViewById<ImageView>(R.id.profile)

        // set status bar color black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        //set status bar white
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        //setting edittext as non-clickable
        name.isEnabled = false
        email.isEnabled = false
        num.isEnabled = false


        // displaying dat into the fields
        if(mAuth.currentUser!!.email==null)
        {
            num.setText(mAuth.currentUser!!.phoneNumber.toString())
            db.collection("users").document(mAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    name.setText(it.getString("name"))
                    if(mAuth.currentUser!!.photoUrl!=null)
                    {
                        val url = mAuth.currentUser!!.photoUrl
                        Glide.with(this)
                            .load(url)
                            .into(image)
                    }
                }
                .addOnFailureListener {
                    name.setText("")
                }
        }
        else
        {
            email.setText(mAuth.currentUser!!.email.toString())
            db.collection("users").document(mAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener {

                    num.setText(it.getString("phone"))
                    if(mAuth.currentUser!!.displayName.isNullOrEmpty())
                    {
                        name.setText(it.getString("name"))
                    }
                    else
                    {
                        name.setText(mAuth.currentUser!!.displayName)
                    }
                    if(mAuth.currentUser!!.photoUrl!=null)
                    {
                        val url = mAuth.currentUser!!.photoUrl
                        Glide.with(this)
                            .load(url)
                            .into(image)
                    }
                }
                .addOnFailureListener {
                    name.setText("")
                }
        }
    }
}