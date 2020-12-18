package com.adazhdw.adapter.core

/**
 * author：adazhdw
 * date-time：2020/12/18 13:49
 * description：辅助SDL方法来执行 RecyclerView.ViewHolder 的默认实现DefaultViewHolder 的相关方法
 **/

interface ViewHolderItemSubscriber {
    var mBindHolderType: ViewHolderType?
    var mUnBindHolderType: ViewHolderType?
    fun onBindViewHolder(bindHolderType: ViewHolderType)
    fun onUnBindViewHolder(unBindHolderType: ViewHolderType)
}