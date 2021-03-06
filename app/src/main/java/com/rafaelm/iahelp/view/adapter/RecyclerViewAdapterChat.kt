package com.rafaelm.iahelp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafaelm.iahelp.R
import com.rafaelm.iahelp.data.savetemp.Constants.Companion.RECEIVER_MSG
import com.rafaelm.iahelp.data.savetemp.Constants.Companion.SEND_MSG
import com.rafaelm.iahelp.data.entity.EntityChat
import kotlinx.android.synthetic.main.chat_left.view.*
import kotlinx.android.synthetic.main.chat_right.view.*

class RecyclerViewAdapterChat(private val chat: List<EntityChat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == SEND_MSG) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_right, parent, false)
            return ViewHolderSend(view)
        }else{
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)
            return ViewHolderReceiver(view)
        }
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chat[position].numberId == 0) {
            SEND_MSG
        } else {
            RECEIVER_MSG
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == SEND_MSG){
            (holder as ViewHolderSend).bind(chat[position])
        }else{
            (holder as ViewHolderReceiver).bind(chat[position])
        }
    }

//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class ViewHolderSend(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(chatSendModel: EntityChat) {
            itemView.txt_chatRight.text = chatSendModel.sendMsg
        }
    }

    inner class ViewHolderReceiver(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(chatReceiverModel: EntityChat) {

            if(chatReceiverModel.receiverMsg.equals("No good match found in KB.", ignoreCase = false)){
                itemView.txt_chatLeft.text = "Desculpa não entendi"
            }else{
                itemView.txt_chatLeft.text = chatReceiverModel.receiverMsg
            }

        }
    }

}