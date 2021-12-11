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
abstract class AbsItem<M : Any, VH : RecyclerView.ViewHolder> : IItem<M, VH> {

    override var data: M? = null

    /**
     * This method returns the ViewHolder for our item, using the provided parent and layoutInflater.
     */
    override fun getViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): VH {
        return getViewHolder(getItemView(parent, layoutInflater))
    }

    /**
     * This method returns the ViewHolder's ItemView for our item, using the provided parent,layoutInflater.
     */
    abstract fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     */
    abstract fun getViewHolder(view: View): VH


    override fun bindVH(holder: VH, payloads: List<Any>) {

    }

    override fun unbindVH(holder: VH) {

    }

    override fun attachToWindow(holder: VH) {

    }

    override fun detachFromWindow(holder: VH) {

    }
}

/**
 * implements a default item by using a default RecyclerView.ViewHolder
 */
open class DefaultItem<M : Any>(@LayoutRes override val layoutRes: Int) : AbsItem<M, DefaultViewHolder>() {

    private var initViewHolder: ViewHolderGetView? = null

    open fun onGetViewHolder(type: ViewHolderGetView) {
        initViewHolder = type
    }

    override fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(layoutRes, parent, false)
    }

    override fun getViewHolder(view: View): DefaultViewHolder {
        return DefaultViewHolder(view).apply { initViewHolder?.invoke(this) }
    }
}



