package com.adazhdw.adapter.core

import androidx.recyclerview.widget.RecyclerView

interface ViewHolderSubscriber {
    fun onBindViewHolder(position: Int, payloads: List<Any>)
    fun unBindViewHolder(position: Int)
    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, position: Int)
    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder, position: Int)
}
