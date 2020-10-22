package com.rafaelm.iahelp.view.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.model.ScreenItem
import com.rafaelm.iahelp.data.savetemp.Prefs
import com.rafaelm.iahelp.view.activity.LoginActivity
import com.rafaelm.iahelp.view.activity.MainActivity
import kotlinx.android.synthetic.main.activity_intro.*


class IntroActivity : AppCompatActivity() {
    var introViewPagerAdapter: IntroViewPagerAdapter? = null
    var tabIndicator: TabLayout? = null
    var btnNext: Button? = null
    var position = 0
    var btnGetStarted: Button? = null
    var btnAnim: Animation? = null
    var tvSkip: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_intro)

        // ini views
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        tvSkip = findViewById(R.id.tv_skip)


        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainActivity)
            finish()
        }


        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        val fresh_food = mList.add(
            ScreenItem(
                "IA HELP",
                "Aplicativo de ajuda!\nCom o aplicativo é possivel ligar para POLICIA ou SAMU somente com a voz.\nApenas para lhe ajudar",
                R.drawable.logo_complete
            )
        )
        mList.add(
            ScreenItem(
                "Ligações de ajuda",
                "Basta você pedir ajuda e a IA definira o tipo de ajuda e ligará para central emergência da sua região.",
                R.drawable.ic_telephone
            )
        )
        mList.add(
            ScreenItem(
                "Problemas visuais",
                "Caso tenha problemas visuais bastar clicar no botão de áudio no canto superior em cima, e você poderá conversar via voz, e recebera a mensagem saira em áudio, caso precise mudar algo clique em configuração no canto superior.",
                R.drawable.ic_voice_command
            )
        )
        mList.add(
            ScreenItem(
                "Mensagem",
                "Ao sacudir o seu telefone será enviado sua localização para número cadastrado com urgência, com pedido de socorro.",
                R.drawable.logo_complete
            )
        )


        // setup viewpager
        val screenPager = findViewById<ViewPager>(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter

        // setup tablayout with viewpager
        tab_indicator.setupWithViewPager(screenPager)

        // Get Started button click listener
        btn_get_started.setOnClickListener(View.OnClickListener {
            val login = Intent(applicationContext, LoginActivity::class.java)
            startActivity(login)
            savePrefsData()
            finish()
        })


        // next button click Listner
        btn_next.setOnClickListener(View.OnClickListener {
            position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.currentItem = position
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }
        })


        // skip button click listener
        tv_skip.setOnClickListener(View.OnClickListener { screenPager.currentItem = mList.size })


        // tablayout add change listener
        tab_indicator.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size - 1) {
                    loaddLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun savePrefsData() {
        val sharedPreferences = Prefs(applicationContext)
        sharedPreferences.save("isIntroOpnend", true)

    }

    private fun restorePrefData(): Boolean {
        val sharedPreferences = Prefs(applicationContext)
        return sharedPreferences.getValueBoolien("isIntroOpnend", false)
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext!!.visibility = View.INVISIBLE
        btnGetStarted!!.visibility = View.VISIBLE
        tvSkip!!.visibility = View.INVISIBLE
        tabIndicator!!.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted!!.animation = btnAnim
    }
}