package com.rafaelm.iahelp.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.entity.EntityChat
import com.rafaelm.iahelp.data.entity.EntityUser
import com.rafaelm.iahelp.data.repository.ChatRepository
import com.rafaelm.iahelp.data.savetemp.Prefs
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity(), MultiplePermissionsListener {

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
                repository.getUser()?.observe(this,{
                    it.forEach {user->
                        if(user.email != email){
                            repository.deleteChat()
                        }
                    }
                })
                btn_singin.visibility = View.VISIBLE
                progress_singin.visibility = View.GONE

                Dexter.withContext(this)
                    .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION,

                        )
                    .withListener(this)
                    .check()

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

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
        val sharedPreferences = Prefs(applicationContext)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        sharedPreferences.save("login", true)

        finish()
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        permissionToken: PermissionToken?
    ) {
        permissionToken?.continuePermissionRequest()
    }


}