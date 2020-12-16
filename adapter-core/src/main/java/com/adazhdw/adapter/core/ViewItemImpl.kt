package com.adazhdw.adapter.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/16 9:08
 * description：ViewItem 的抽象实现类
 **/

abstract class AbsViewItem<M, VH : RecyclerView.ViewHolder> : ViewItem<M, VH> {
    override var model: M? = null

    /**
     * implement ItemVHFactory's function getViewHolder
     */
    override fun getViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): VH {
        return getViewHolder(getHolderItemView(parent, layoutInflater))
    }

    /**
     * Gets the ViewHolder's ItemView
     */
    abstract fun getHolderItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View

    /**
     * Gets the real ViewHolder
     */
    abstract fun getViewHolder(itemView: View): VH

}

abstract class LayoutViewItem<M, VH : RecyclerView.ViewHolder>(override val layoutRes: Int) : AbsViewItem<M, VH>() {
    override fun getHolderItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(layoutRes, parent, false)
    }
}

open class DefaultViewItem<M>(override val layoutRes: Int) : LayoutViewItem<M, DefaultViewHolder>(layoutRes), ViewHolderItemSubscriber {

    override fun getViewHolder(itemView: View): DefaultViewHolder = DefaultViewHolder(itemView)

    override fun bindVH(holder: DefaultViewHolder, payloads: List<Any>) {
        mBindHolderType?.invoke(holder)
    }

    override fun unBindVH(holder: DefaultViewHolder) {
        mUnBindHolderType?.invoke(holder)
    }

    override var mBindHolderType: ViewHolderType? = null
    override var mUnBindHolderType: ViewHolderType? = null

    override fun onBindViewHolder(bindHolderType: ViewHolderType) {
        mBindHolderType = bindHolderType
    }

    override fun onUnBindViewHolder(unBindHolderType: ViewHolderType) {
        mUnBindHolderType = unBindHolderType
    }
}





