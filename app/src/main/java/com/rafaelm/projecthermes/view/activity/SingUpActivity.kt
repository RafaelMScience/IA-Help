package com.rafaelm.projecthermes.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.model.firebase.User
import com.rafaelm.projecthermes.functions.Mask
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.*
import kotlin.collections.HashMap

class SingUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val email = edtInput_email_signup.text.toString().toLowerCase(Locale.ROOT)
        val number = edtInput_number_smartphone_signup.text.toString().toLowerCase(Locale.ROOT)
        val password = edtInput_password_singup.text.toString().toLowerCase(Locale.ROOT)
        val name = edtInput_name_signup.text.toString().toLowerCase(Locale.ROOT)
        val login = edtInput_login_singup.text.toString().toLowerCase(Locale.ROOT)

        val user = User(email = email,number_phone = number, password = password, name = name, login = login)
        edtInput_number_smartphone_signup.addTextChangedListener(Mask.mask("(##)#.####-####", edtInput_number_smartphone_signup))

        btn_singup.setOnClickListener {
            saveDatabaseApi()
        }
    }

    fun saveDatabaseApi() {
        val db = FirebaseFirestore.getInstance()

        val email = edtInput_email_signup.text.toString().toLowerCase(Locale.ROOT)

        val user : MutableMap<String, String> = HashMap()
        user["email"] = email
        user["name"] = edtInput_name_signup.text.toString().toLowerCase(Locale.ROOT)

        db.collection("users")
            .get()
            .addOnCompleteListener {
                if (it.result!!.isEmpty) {
                    db.collection("users").document(email)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@SingUpActivity,
                                "funciona ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@SingUpActivity,
                                "falhou",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                } else {
                    for (document in it.result!!) {
                        if (document.id != email.toLowerCase() || document.id.isEmpty())
                            db.collection("users").document(email)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@SingUpActivity,
                                        "funciona ",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@SingUpActivity,
                                        "falhou",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }
                    }

                }
            }
            .addOnFailureListener {
                Toast.makeText(this@SingUpActivity, "falhou", Toast.LENGTH_SHORT).show()

            }
    }

}
