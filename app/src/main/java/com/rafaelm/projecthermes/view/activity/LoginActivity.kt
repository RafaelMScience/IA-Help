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
import com.rafaelm.projecthermes.data.repository.ChatRepository
import com.rafaelm.projecthermes.data.savetemp.Prefs
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = Prefs(applicationContext)

        val logado = sharedPreferences.getValueBoolien("login", false)
        if(logado) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun singUp(view: View) {
        val i = Intent(this, SingUpActivity::class.java)
        startActivity(i)
    }

    fun singIn(view: View) {
        btn_singin.visibility = View.GONE
        progress_singin.visibility = View.VISIBLE

        val sharedPreferences = Prefs(applicationContext)

        val email = edtInput_login_singin.text.toString().toLowerCase(Locale.ROOT)
        val password = edtInput_password_singin.text.toString()
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(email)

        val repository = ChatRepository(application)

        docRef.get().addOnSuccessListener {document ->
            if (document.getString("password").equals(password)) {

                val user = EntityUser(
                    email = document.getString("email").toString(),
                    name = document.getString("name").toString(),
                    numberPhone = document.getString("number_phone").toString()
                )

                repository.insertUser(user)
                btn_singin.visibility = View.VISIBLE
                progress_singin.visibility = View.GONE

                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                sharedPreferences.save("login", true)
                finish()
            } else {
                Toast.makeText(this, "Verifique seu Email/Password", Toast.LENGTH_SHORT).show()
                btn_singin.visibility = View.VISIBLE
                progress_singin.visibility = View.GONE
            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"Verifique sua conexao", Toast.LENGTH_SHORT).show()
                btn_singin.visibility = View.VISIBLE
                progress_singin.visibility = View.GONE
            }
    }
}