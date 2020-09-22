package com.rafaelm.projecthermes.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.api.RetrofitAzure
import com.rafaelm.projecthermes.data.api.RetrofitChatbot
import com.rafaelm.projecthermes.data.entity.EntityChat
import com.rafaelm.projecthermes.data.model.chatbot.AnswerResponse
import com.rafaelm.projecthermes.data.model.chatbot.ChatRequest
import com.rafaelm.projecthermes.data.model.luis.ModelAzure
import com.rafaelm.projecthermes.data.repository.ChatRepository
import com.rafaelm.projecthermes.data.savetemp.Constants.Companion.RQ_SPEECH_REC
import com.rafaelm.projecthermes.data.savetemp.Constants.Companion.keyChatAuthBotApi
import com.rafaelm.projecthermes.data.savetemp.Constants.Companion.keyLuis
import com.rafaelm.projecthermes.data.savetemp.Prefs
import com.rafaelm.projecthermes.functions.ShakeDetector
import com.rafaelm.projecthermes.view.adapter.RecyclerViewAdapterChat
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.util.*

class MainActivity : AppCompatActivity(), MultiplePermissionsListener, LocationListener {

    private lateinit var mTTs: TextToSpeech
    private var audioMenu = true


    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onStart() {

        val repository = ChatRepository(application)
        recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
        repository.getChat()?.observe(this, androidx.lifecycle.Observer {
            recyclerview_chat.adapter = RecyclerViewAdapterChat(it)
        })

        btn_speech.text = "Falar"
        val icMicDrawable = getDrawable(R.drawable.ic_mic)
        val h: Int = icMicDrawable!!.intrinsicHeight
        val w: Int = icMicDrawable.intrinsicWidth
        icMicDrawable.setBounds(0, 0, w, h)
        btn_speech.setCompoundDrawables(null, null, icMicDrawable, null)
        btn_speech.setOnClickListener {
            askSpeechInput()
        }

        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main as Toolbar?)
        supportActionBar?.title = "IA CHAT DE AJUDA"

        //mudar cor status
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,

                )
            .withListener(this)
            .check()

        clickButtonSend()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {

                getLocation()
                Toast.makeText(this@MainActivity, "Enviando sua localizacao", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        mTTs = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                mTTs.language = Locale.ROOT
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try {
            val locationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                5f,
                this@MainActivity
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(
            this,
            "" + location.latitude.toString() + "," + location.longitude.toString(),
            Toast.LENGTH_SHORT
        ).show()
        try {
            val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address: String = addresses[0].getAddressLine(0)
            val uri =
                "http://maps.google.com/maps?daddr=" + location.latitude.toString() + "," + location.longitude.toString()

            val packageManager: PackageManager = packageManager
            val sharingIntent = Intent(Intent.ACTION_VIEW)
            sharingIntent.type = "text/plain"
            val phone = "5592991211156"

            val shareSub = "Socorro preciso de ajuda estou na:\n$address\nMapa: $uri"
            val url =
                "https://api.whatsapp.com/send?phone=$phone&text=" + URLEncoder.encode(
                    shareSub,
                    "UTF-8"
                )
//            sharingIntent.putExtra(
//                Intent.EXTRA_TEXT, "" + ShareSub.trimIndent()
//            )
            sharingIntent.data = Uri.parse(url)
            sharingIntent.setPackage("com.whatsapp")

            if (sharingIntent.resolveActivity(packageManager) != null) {
                startActivity(sharingIntent)
            }
//            startActivity(Intent.createChooser(sharingIntent, "Share With?"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onResume() {
        super.onResume()

        mSensorManager!!.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(mShakeDetector)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_audio -> {
                val sharedPreferences = Prefs(applicationContext)

                if (audioMenu) {
                    audioMenu = false
                    sharedPreferences.save("audio_menu", audioMenu)
                    Toast.makeText(this, "Audio Desativado", Toast.LENGTH_SHORT).show()
                    item.icon = getDrawable(R.drawable.ic_volume_off)
                    edt_msg.visibility = View.VISIBLE
                    btn_speech.visibility = View.VISIBLE

                    card_speak.visibility = View.GONE

                } else {
                    audioMenu = true
                    sharedPreferences.save("audio_menu", audioMenu)
                    Toast.makeText(this, "Audio Ativado", Toast.LENGTH_SHORT).show()
                    item.icon = getDrawable(R.drawable.ic_hearing)
                    edt_msg.visibility = View.GONE
                    btn_speech.visibility = View.GONE

                    card_speak.visibility = View.VISIBLE

                    card_speak.setOnClickListener {
                        askSpeechInput()
                    }

                }
                return !audioMenu
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clickButtonSend() {
        edt_msg.addTextChangedListener {
            if (it?.length == 0) {
                btn_speech.text = "Falar"
                val icMicDrawable = getDrawable(R.drawable.ic_mic)
                val h: Int = icMicDrawable!!.intrinsicHeight
                val w: Int = icMicDrawable.intrinsicWidth
                icMicDrawable.setBounds(0, 0, w, h)
                btn_speech.setCompoundDrawables(null, null, icMicDrawable, null)
                btn_speech.setOnClickListener {
                    askSpeechInput()
                }
            } else {
                val icSendDrawable = getDrawable(R.drawable.ic_send)
                val h: Int = icSendDrawable!!.intrinsicHeight
                val w: Int = icSendDrawable.intrinsicWidth
                icSendDrawable.setBounds(0, 0, w, h)
                btn_speech.setCompoundDrawables(null, null, icSendDrawable, null)
                btn_speech.text = "Enviar"
                btn_speech.setOnClickListener {
                    chatbot(edt_msg.text.toString())
                    conectionApi(edt_msg.text.toString())
                    edt_msg.text.clear()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textResult = result?.get(0).toString()

            chatbot(textResult)
            conectionApi(textResult)
        }
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Fala nao liberado", Toast.LENGTH_SHORT).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale")
            startActivityForResult(i, RQ_SPEECH_REC)
        }
    }

    private fun conectionApi(textResult: String) {

        val data: MutableMap<String, String> = HashMap()
        data["subscription-key"] = keyLuis
        data["verbose"] = true.toString()
        data["show-all-intents"] = true.toString()
        data["log"] = true.toString()
        data["query"] = textResult

        val sharedPreferences = Prefs(applicationContext)
        val retrofitClient = RetrofitAzure.apiConnection().postSendText(data)

        retrofitClient.enqueue(object : Callback<ModelAzure> {
            override fun onResponse(call: Call<ModelAzure>, response: Response<ModelAzure>) {
                val type = response.body()?.prediction?.topIntent.toString()

                sharedPreferences.save(
                    "prediction",
                    response.body()?.prediction?.topIntent.toString()
                )

                val emergency = "111"
                val policy = "222"

                Dexter.withContext(this@MainActivity)
                    .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CALL_PHONE
                    )
                    .withListener(this@MainActivity)
                    .check()

                Log.i("teste", type.toString())

                when {
                    type.equals("Saude emergência", ignoreCase = true) -> {
                        val i = Intent(Intent.ACTION_CALL)
                        i.data = Uri.parse("tel: $emergency")
                        startActivity(i)
                    }
                    type.equals("Saude urgencia", ignoreCase = true) -> {
                        val i = Intent(Intent.ACTION_CALL)
                        i.data = Uri.parse("tel: $emergency")
                        startActivity(i)
                    }
                    type.equals("Casos de policia", ignoreCase = true) -> {
                        val i = Intent(Intent.ACTION_CALL)
                        i.data = Uri.parse("tel: $policy")
                        startActivity(i)
                    }
                }

            }

            override fun onFailure(call: Call<ModelAzure>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falhou", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun chatbot(textResult: String) {

        val chatApi = ChatRequest(textResult)


        val repository = ChatRepository(application)
        val retrofitChatbot = RetrofitChatbot.apiConnection().postChat(keyChatAuthBotApi, chatApi)

        val chatEntity =
            EntityChat(receiverMsg = null, numberId = 0, sendMsg = textResult, typeMsg = null)
        repository.insetChat(chatEntity)

        val sharedPreferences = Prefs(applicationContext)
        retrofitChatbot.enqueue(object : Callback<AnswerResponse> {
            override fun onResponse(
                call: Call<AnswerResponse>,
                response: Response<AnswerResponse>
            ) {

                response.body()?.answers?.forEach {
                    val typeMsg = sharedPreferences.getValueString("prediction")
                    val chatReceiver = EntityChat(
                        receiverMsg = it.answer,
                        numberId = 1,
                        sendMsg = null,
                        typeMsg = typeMsg
                    )
                    repository.insetChat(chatReceiver)

                    val audioBool = sharedPreferences.getValueBoolien("audio_menu", false)
                    if (audioBool) {
                        mTTs.speak(it.answer, TextToSpeech.QUEUE_FLUSH, null)
                    }
                }
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
        repository.getChat()?.observe(this, androidx.lifecycle.Observer {
            recyclerview_chat.adapter = RecyclerViewAdapterChat(it)
        })
    }

    override fun onPermissionsChecked(permission: MultiplePermissionsReport?) {
        clickButtonSend()

    }

    override fun onPermissionRationaleShouldBeShown(
        permissionReq: MutableList<PermissionRequest>?,
        permissionToken: PermissionToken?
    ) {
        permissionToken?.continuePermissionRequest()
    }
}