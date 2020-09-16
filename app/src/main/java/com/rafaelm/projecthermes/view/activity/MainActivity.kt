package com.rafaelm.projecthermes.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.api.RetrofitAzure
import com.rafaelm.projecthermes.data.api.RetrofitChatbot
import com.rafaelm.projecthermes.data.dao.Constants.Companion.RQ_SPEECH_REC
import com.rafaelm.projecthermes.data.dao.Constants.Companion.keyLuis
import com.rafaelm.projecthermes.data.dao.Constants.Companion.keyChatAuthBotApi
import com.rafaelm.projecthermes.data.dao.Prefs
import com.rafaelm.projecthermes.data.entity.EntityChat
import com.rafaelm.projecthermes.data.model.chatbot.AnswerResponse
import com.rafaelm.projecthermes.data.model.chatbot.ChatRequest
import com.rafaelm.projecthermes.data.model.luis.ModelAzure
import com.rafaelm.projecthermes.data.repository.ChatRepository
import com.rafaelm.projecthermes.view.adapter.RecyclerViewAdapterChat
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), MultiplePermissionsListener {


    override fun onStart() {

        val repository = ChatRepository(application)
        recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
        repository.getChat()?.observe(this, androidx.lifecycle.Observer {
            recyclerview_chat.adapter = RecyclerViewAdapterChat(it)
        })
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_speech.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO,Manifest.permission.CALL_PHONE)
                .withListener(this)
                .check()
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

                sharedPreferences.save("prediction", response.body()?.prediction?.topIntent.toString())

                val emergency = "111"

                if (type.equals("Saude emergência", ignoreCase = true)){
                    val i = Intent(Intent.ACTION_CALL)
                    i.data = Uri.parse("tel: $emergency")
                    startActivity(i)
                }else if (type.equals("Saude urgencia", ignoreCase = true)){
                    val i = Intent(Intent.ACTION_CALL)
                    i.data= Uri.parse("tel: $emergency")
                    startActivity(i)
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

        val chatEntity = EntityChat(receiverMsg = null, numberId = 0,sendMsg = textResult,typeMsg = null)
        repository.insetChat(chatEntity)

        val sharedPreferences = Prefs(applicationContext)
        retrofitChatbot.enqueue(object: Callback<AnswerResponse> {
            override fun onResponse(
                call: Call<AnswerResponse>,
                response: Response<AnswerResponse>
            ) {

                response.body()?.answers?.forEach {
                    val typeMsg = sharedPreferences.getValueString("prediction")
                    val chatReceiver = EntityChat(receiverMsg = it.answer, numberId = 1,sendMsg = null,typeMsg = typeMsg)
                    repository.insetChat(chatReceiver)
//                    recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
//                    recyclerview_chat.adapter = RecyclerViewAdapterChat(it.answer)
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
        askSpeechInput()

    }

    override fun onPermissionRationaleShouldBeShown(
        permissionReq: MutableList<PermissionRequest>?,
        permissionToken: PermissionToken?
    ) {
        permissionToken?.continuePermissionRequest()
    }
}