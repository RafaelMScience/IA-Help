package com.rafaelm.projecthermes.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.entity.EntityUser
import com.rafaelm.projecthermes.data.model.firebase.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun singUp(view: View) {
        val i = Intent(this, SingUpActivity::class.java)
        startActivity(i)
    }

    fun singIn(view: View) {
        val email = edtInput_login_singin.text.toString().toLowerCase()
        val password = edtInput_password_singin.text.toString()
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(email)

        docRef.get().addOnSuccessListener {
            if (it.getString("password").equals(password)) {

                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Verifique seu Email/Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}