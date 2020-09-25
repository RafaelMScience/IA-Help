package com.rafaelm.iahelp.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.entity.EntityUser
import com.rafaelm.iahelp.data.repository.ChatRepository
import com.rafaelm.iahelp.functions.Mask
import kotlinx.android.synthetic.main.activity_configure.*
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.*


class ConfigureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure)
        val repository = ChatRepository(application)

        setSupportActionBar(toolbar_config as Toolbar?)
        supportActionBar?.title = "ATUALIZAR"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtInput_number_smartphone_config.addTextChangedListener(
            Mask.mask(
                "(##)#####-####",
                edtInput_number_smartphone_config
            )
        )

        repository.getUser()?.observe(this, { listUser ->
            listUser.forEach { user ->
                txt_email_config.text = user.email
                edtInput_name_config.hint = user.name
                edtInput_number_smartphone_config.hint = user.numberPhone
                btn_config.setOnClickListener {
                    btn_config.visibility = View.GONE
                    progress_config.visibility = View.VISIBLE
                    updateDateUser(user.email)
                }
            }

        })

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

    private fun updateDateUser(email: String) {
        val repository = ChatRepository(application)

        val user = mutableMapOf<String, String>()

        val number = edtInput_number_smartphone_config.text.toString().toLowerCase(Locale.ROOT)
        val password = edtInput_password_config.text.toString()
        val name = edtInput_name_config.text.toString().toLowerCase(Locale.ROOT)

        when {
            name.isNotEmpty() && name.length > 2 -> {
                user["name"] = name
            }
            name.isNotEmpty() && name.length <=2 -> {
                edtInput_name_config.error = "Minimo é 2 digitos para seu nome"
            }
        }


        when {
            password.isNotEmpty() && password.length > 4 -> {
                user["password"] = password
            }
            password.isNotEmpty() && password.length <=4 -> {
                edtInput_password_config.error = "Informe uma senha de no minimo 5 digitos"
            }
        }

        when {
            number.isNotEmpty() && number.length == 14 -> {
                user["number_phone"] = number
            }
            number.isNotEmpty() && number.length != 14 -> {
                edtInput_number_smartphone_config.error = "Informe numero correto"
            }
        }

        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(email)
            .set(user, SetOptions.merge())

        val userDatabase = EntityUser(
            email = email,
            numberPhone = number,
            name = name,
        )

        repository.insertUser(userDatabase)

        btn_config.visibility = View.VISIBLE
        progress_config.visibility = View.GONE

    }
}




