package com.rafaelm.iahelp.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.entity.EntityUser
import com.rafaelm.iahelp.data.model.firebase.User
import com.rafaelm.iahelp.data.repository.ChatRepository
import com.rafaelm.iahelp.functions.Mask
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
            val passwordConfirm = edtInput_password_confirm_singup.text.toString()
            val name = edtInput_name_signup.text.toString().toLowerCase(Locale.ROOT)
            val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            when {

                name.isEmpty() || name.length <= 2 -> {
                    edtInput_name_signup.error = "Digite o nome"
                }
                email.isEmpty() || email.length <= 4 || !emailValid -> {
                    edtInput_email_signup.error = "Digite o email corretamente"
                }
                number.length != 14 || number.isEmpty() -> {
                    edtInput_number_smartphone_signup.error = "Digite o numero correto"
                }
                password.isEmpty() || password.length <= 4 -> {
                    edtInput_password_singup.error = "Digite o password no minimo"
                }
                passwordConfirm.isEmpty() || passwordConfirm != password -> {
                    edtInput_password_confirm_singup.error = "Password não esta igual"
                }

                else -> {
                    val user = User(
                        email = email,
                        number_phone = number,
                        password = password,
                        name = name,
                    )
                    saveDatabaseApi(user)
//                    val userEntity = EntityUser(
//                        email = email,
//                        name = name,
//                        numberPhone = number
//                    )
//
//                    repository.insertUser(userEntity)

                }

            }

            btn_singup.visibility = View.VISIBLE
            progress_signup.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveDatabaseApi(user: User) {
        val db = FirebaseFirestore.getInstance()
        val repository = ChatRepository(application)


        val docRef = db.collection("users").document(user.email)

        docRef.get().addOnSuccessListener { document ->
            when {
                document?.exists() == false -> {
                    db.collection("users").document(user.email)
                        .set(user)
                        .addOnSuccessListener {
                            val userEntity = EntityUser(
                                email = user.email,
                                name = user.name,
                                numberPhone = user.number_phone
                            )

                            repository.insertUser(userEntity)
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                            finish()
                            Toast.makeText(
                                this,
                                "Cadastrado com sucesso\nseja bem vindo: ${user.name}",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
