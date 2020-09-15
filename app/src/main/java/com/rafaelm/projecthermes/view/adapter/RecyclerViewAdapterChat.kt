package com.rafaelm.projecthermes.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafaelm.projecthermes.R
import com.rafaelm.projecthermes.data.entity.EntityChat
import kotlinx.android.synthetic.main.chat_left.view.*


class RecyclerViewAdapterChat(private val answer: List<EntityChat>) : RecyclerView.Adapter<RecyclerViewAdapterChat.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_left,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return answer.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answernu = answer[position]
        holder.itemView.txt_chatLeft.text = answernu.receiverMsg
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}