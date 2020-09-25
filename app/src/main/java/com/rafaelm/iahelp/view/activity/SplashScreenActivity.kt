package com.rafaelm.iahelp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.savetemp.Prefs

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorBackgroundWhite)

        val sharedPreferences = Prefs(applicationContext)

        val logado = sharedPreferences.getValueBoolien("login", false)
        if(logado) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        val handle = Handler()
        handle.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}