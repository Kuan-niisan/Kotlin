package com.example.calculator

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(private val emailList: MutableList<Email>) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    inner class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvPreview: TextView = itemView.findViewById(R.id.tvPreview)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val ivStar: ImageView = itemView.findViewById(R.id.ivStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val email = emailList[position]
        holder.tvSender.text = email.sender
        holder.tvSubject.text = email.subject
        holder.tvPreview.text = email.preview
        holder.tvTime.text = email.time
        holder.tvAvatar.text = email.senderInitial.toString()

        val background = holder.tvAvatar.background as GradientDrawable
        background.setColor(email.avatarColor)

        if (email.isStarred) {
            holder.ivStar.setImageResource(R.drawable.ic_star_filled)
        } else {
            holder.ivStar.setImageResource(R.drawable.ic_star_border)
        }

        // Sự kiện click cho ngôi sao
        holder.ivStar.setOnClickListener {
            email.isStarred = !email.isStarred
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = emailList.size
}