package com.adazhdw.adapter.core

/**
 * author：daguozhu
 * date-time：2020/12/16 13:22
 * description：
 **/

interface ViewHolderItemSubscriber {
    var mBindHolderType: ViewHolderType?
    var mUnBindHolderType: ViewHolderType?
    fun onBindViewHolder(bindHolderType: ViewHolderType)
    fun onUnBindViewHolder(unBindHolderType: ViewHolderType)
}