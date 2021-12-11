package com.adazhdw.adapter.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * author：adazhdw
 * date-time：2020/12/21 10:18
 * description：
 **/
open class DefaultViewHolder(itemView: View) : BaseViewHolder(itemView), VHSubscriber {

    private var bindView: ViewHolderBindView? = null
    private var unBindType: ViewHolderType? = null
    private var attachType: ViewHolderType? = null
    private var detachType: ViewHolderType? = null

    fun onBindViewHolder(bindView: ViewHolderBindView) {
        this.bindView = bindView
    }

    fun unBindViewHolder(type: ViewHolderType) {
        this.unBindType = type
    }

    fun onViewAttachedToWindow(type: ViewHolderType) {
        this.attachType = type
    }

    fun onViewDetachedFromWindow(type: ViewHolderType) {
        this.detachType = type
    }

    override fun onBindViewHolder(position: Int, payloads: List<Any>) {
        this.bindView?.invoke(this, position, payloads)
    }

    override fun unBindViewHolder(position: Int) {
        this.unBindType?.invoke(this, position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, position: Int) {
        this.attachType?.invoke(holder as DefaultViewHolder, position)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder, position: Int) {
        this.detachType?.invoke(holder as DefaultViewHolder, position)
    }

    fun bindAvailable(position: Int): Boolean {
        return position >= 0
    }
}

