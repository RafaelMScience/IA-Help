package com.rafaelm.projecthermes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.rafaelm.projecthermes.data.api.RetrofitAzure
import com.rafaelm.projecthermes.data.api.RetrofitChatbot
import com.rafaelm.projecthermes.data.dao.Constants.Companion.RQ_SPEECH_REC
import com.rafaelm.projecthermes.data.dao.Constants.Companion.key
import com.rafaelm.projecthermes.data.dao.Constants.Companion.keyChatBotApi
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

class MainActivity : AppCompatActivity(), PermissionListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_speech.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(this)
                .check()
        }

        btn_chat.setOnClickListener {
//            chatbot(textResult)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textResult = result?.get(0).toString()

            chatbot(textResult)
//            conectionApi(textResult)
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
        data["subscription-key"] = key
        data["verbose"] = true.toString()
        data["show-all-intents"] = true.toString()
        data["log"] = true.toString()
        data["query"] = textResult

//        val textResultModel = ModelAzure(query = textResult,)

        val retrofitClient = RetrofitAzure.apiConnection().postSendText(data)

        retrofitClient.enqueue(object : Callback<ModelAzure> {
            override fun onResponse(call: Call<ModelAzure>, response: Response<ModelAzure>) {

                txt_speechText.text = response.body()?.prediction?.topIntent.toString()

            }

            override fun onFailure(call: Call<ModelAzure>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falhou", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        askSpeechInput()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        //se negar fica nessa tela pedindo permissao
        Toast.makeText(this,"Permissao para falar negada", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest()
    }

    private fun chatbot(textResult: String) {

        val testeApi = ChatRequest(textResult)


        val repository = ChatRepository(application)
        val retrofitChatbot = RetrofitChatbot.apiConnection().postChat(keyChatBotApi, testeApi)

        val chat = EntityChat(receiverMsg = textResult, number_receiver = 1, number_send = null,sendMsg = null)
        repository.insetChat(chat)

        Log.d("teste",chat.toString())


//        var sendchat: List<EntityChat>? = null
////        Thread(Runnable {
////            sendchat = repository.getChat()
////        }).start()
////
////
////        sendchat?.forEach{
////            txt_speechText.text = it.sendMsg
////        }

        recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
        repository.getChat()?.observe(this, androidx.lifecycle.Observer {
            Log.i("teste", it.toString())
            recyclerview_chat.adapter = RecyclerViewAdapterChat(it)
        })
        retrofitChatbot.enqueue(object: Callback<AnswerResponse> {
            override fun onResponse(
                call: Call<AnswerResponse>,
                response: Response<AnswerResponse>
            ) {


                response.body()?.answers?.forEach {
//                    recyclerview_chat.layoutManager = LinearLayoutManager(applicationContext)
//                    recyclerview_chat.adapter = RecyclerViewAdapterChat(it.answer)
                }
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}