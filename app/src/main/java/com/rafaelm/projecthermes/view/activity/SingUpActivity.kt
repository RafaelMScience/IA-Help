package com.rafaelm.projecthermes.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.model.firebase.User
import com.rafaelm.projecthermes.data.repository.ChatRepository
import com.rafaelm.projecthermes.functions.Mask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.*

class SingUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        setSupportActionBar(toolbar_singup as Toolbar?)
        supportActionBar?.title = "CADASTRO"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        edtInput_number_smartphone_signup.addTextChangedListener(
            Mask.mask(
                "(##)#####-####",
                edtInput_number_smartphone_signup
            )
        )

        btn_singup.setOnClickListener {
            btn_singup.visibility = View.GONE
            progress_signup.visibility = View.VISIBLE
            val email = edtInput_email_signup.text.toString().toLowerCase(Locale.ROOT)
            val number = edtInput_number_smartphone_signup.text.toString().toLowerCase(Locale.ROOT)
            val password = edtInput_password_singup.text.toString()
            val name = edtInput_name_signup.text.toString().toLowerCase(Locale.ROOT)

            if (email.isNotEmpty() && number.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {

                val user = User(
                    email = email,
                    number_phone = number,
                    password = password,
                    name = name,
                )
                saveDatabaseApi(user)

            }else{
                Toast.makeText(this,"Preencha todos os campos porfavor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveDatabaseApi(user: User) {
        val db = FirebaseFirestore.getInstance()

       val docRef = db.collection("users").document(user.email)

        docRef.get().addOnSuccessListener {document->
            when {
                document?.exists() == false -> {
                    db.collection("users").document(user.email)
                        .set(user)
                        .addOnSuccessListener {
                            btn_singup.visibility = View.VISIBLE
                            progress_signup.visibility = View.GONE
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@SingUpActivity,
                                "Verifique sua conexao com a internet",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            btn_singup.visibility = View.VISIBLE
                            progress_signup.visibility = View.GONE

                        }
                }
                document != null -> {
                    Toast.makeText(this, "Email ja cadastrado", Toast.LENGTH_SHORT).show()
                    btn_singup.visibility = View.VISIBLE
                    progress_signup.visibility = View.GONE

                }
                else -> {
                    Toast.makeText(this,"Erro",Toast.LENGTH_SHORT).show()
                }
            }
       }

    }

}
