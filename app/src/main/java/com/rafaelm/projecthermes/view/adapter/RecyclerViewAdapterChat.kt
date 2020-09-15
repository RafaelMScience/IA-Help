package com.rafaelm.projecthermes.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafaelm.projecthermes.R
import kotlinx.android.synthetic.main.chat_left.view.*


class RecyclerViewAdapterChat(val answer: String) : RecyclerView.Adapter<RecyclerViewAdapterChat.ViewHolder>(){

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
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txt_chatLeft.text = answer
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}