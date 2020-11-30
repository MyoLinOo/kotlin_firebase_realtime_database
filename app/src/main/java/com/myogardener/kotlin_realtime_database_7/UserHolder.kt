package com.myogardener.kotlin_realtime_database_7

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User?) {
        with(user!!) {
            itemView.item_age.text = age
        }
    }
}