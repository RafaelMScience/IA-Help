package com.rafaelm.projecthermes.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.rafaelm.projecthermes.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun singUp(view: View) {
        val i = Intent(this,SingUpActivity::class.java)
        startActivity(i)
    }

    fun singIn(view: View) {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}