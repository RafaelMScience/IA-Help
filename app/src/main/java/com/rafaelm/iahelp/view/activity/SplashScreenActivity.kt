package com.rafaelm.iahelp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.content.ContextCompat
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.savetemp.Prefs
import com.rafaelm.iahelp.view.intro.IntroActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorBackgroundWhite)

        val sharedPreferences = Prefs(applicationContext)

        val logado = sharedPreferences.getValueBoolien("login", false)
        val intro = sharedPreferences.getValueBoolien("isIntroOpnend", false)

        when {
            logado -> {

                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
            !intro -> {
                val handle = Handler()
                handle.postDelayed({
                    val intent = Intent(this, IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
            else -> {
                val handle = Handler()
                handle.postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }
    }
}