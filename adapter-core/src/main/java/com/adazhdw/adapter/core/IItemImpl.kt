package com.adazhdw.adapter.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * author：adazhdw
 * date-time：2020/12/18 9:40
 * description：IItem 抽象实现类
 **/

/**
 * Implements the general methods of the IItem interface to speed up development.
 */
abstract class AbsItem<M : Any> : IItem<M, DefaultViewHolder> {

    override var data: M? = null
    private var initViewHolder: ViewHolderType? = null

    open fun onGetViewHolder(type: ViewHolderType) {
        initViewHolder = type
    }

    /**
     * This method returns the ViewHolder for our item, using the provided parent and layoutInflater.
     */
    override fun getViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): DefaultViewHolder {
        return getViewHolder(getItemView(parent, layoutInflater)).apply { initViewHolder?.invoke(this) }
    }

    /**
     * This method returns the ViewHolder's ItemView for our item, using the provided parent,layoutInflater.
     */
    abstract fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     */
    open fun getViewHolder(view: View): DefaultViewHolder {
        return DefaultViewHolder(view)
    }

    override fun bindVH(holder: DefaultViewHolder, payloads: List<Any>) {

    }

    override fun unbindVH(holder: DefaultViewHolder) {

    }

    override fun attachToWindow(holder: DefaultViewHolder) {

    }

    override fun detachFromWindow(holder: DefaultViewHolder) {

    }
}

/**
 * implements a default item by using a default RecyclerView.ViewHolder
 */
open class DefaultItem<M : Any>(@LayoutRes override val layoutRes: Int) : AbsItem<M>() {

    override fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(layoutRes, parent, false)
    }
}



