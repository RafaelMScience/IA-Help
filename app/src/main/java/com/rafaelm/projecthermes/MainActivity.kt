package com.rafaelm.projecthermes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.rafaelm.projecthermes.data.dao.Constants.Companion.key
import com.rafaelm.projecthermes.data.dao.Constants.Companion.RQ_SPEECH_REC
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rafaelm.projecthermes.data.api.RetrofitAzure
import com.rafaelm.projecthermes.data.model.ModelAzure
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_speech.setOnClickListener {
            askSpeechInput()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textResult = result?.get(0).toString()

            conectionApi(textResult)
        }
    }

    private fun askSpeechInput(){
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Fala nao liberado", Toast.LENGTH_SHORT).show()
        }else{
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
}