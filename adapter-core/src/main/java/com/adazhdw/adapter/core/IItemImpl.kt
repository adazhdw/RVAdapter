package com.adazhdw.adapter.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * author：adazhdw
 * date-time：2020/12/18 9:40
 * description：IItem 抽象实现类
 **/

/**
 * Implements the general methods of the IItem interface to speed up development.
 */
abstract class AbsItem<M, VH : RecyclerView.ViewHolder> : IItem<M, VH> {

    override var data: M? = null

    override fun bindVH(holder: VH, payloads: List<M>) {

    }

    override fun unbindVH(holder: VH) {

    }

    override fun attachToWindow(holder: VH) {

    }

    override fun detachFromWindow(holder: VH) {

    }

    override fun getViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): VH {
        return getViewHolder(getItemView(parent, layoutInflater))
    }

    /**
     * This method returns the ViewHolder's ItemView for our item, using the provided parent,layoutInflater.
     *
     * @return the ViewHolder for this Item
     */
    abstract fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    abstract fun getViewHolder(view: View): VH
}


/**
 * implements AbsItem and its getItemView fun
 */
abstract class LayoutItem<M, VH : RecyclerView.ViewHolder>(override val layoutRes: Int) : AbsItem<M, VH>() {

    override fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(layoutRes, parent, false)
    }
}

/**
 * implements a default item by using a default RecyclerView.ViewHolder
 */
open class DefaultItem<M>(@LayoutRes layoutRes: Int) : LayoutItem<M, DefaultViewHolder>(layoutRes), ViewHolderItemSubscriber {

    override fun getViewHolder(view: View): DefaultViewHolder {
        return DefaultViewHolder(view)
    }

    override fun bindVH(holder: DefaultViewHolder, payloads: List<M>) {
        super.bindVH(holder, payloads)
        this.mBindHolderType?.invoke(holder)
    }

    override fun unbindVH(holder: DefaultViewHolder) {
        super.unbindVH(holder)
        this.mUnBindHolderType?.invoke(holder)
    }

    override var mBindHolderType: ViewHolderType? = null
    override var mUnBindHolderType: ViewHolderType? = null

    override fun onBindViewHolder(bindHolderType: ViewHolderType) {
        this.mBindHolderType = bindHolderType
    }

    override fun onUnBindViewHolder(unBindHolderType: ViewHolderType) {
        this.mUnBindHolderType = unBindHolderType
    }
}



