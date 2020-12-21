package com.adazhdw.adapter.core

import androidx.annotation.LayoutRes

/**
 * author：adazhdw
 * date-time：2020/12/16 9:56
 * description：
 **/

inline fun listAdapter(block: ListAdapter.() -> Unit): ListAdapter {
    return ListAdapter().apply { block() }
}

inline fun <M : Any> defaultItem(
    @LayoutRes layoutRes: Int,
    model: M,
    crossinline bind: DefaultViewHolder.() -> Unit
) = DefaultItem<M>(layoutRes).apply {
    this.data = model
    onGetViewHolder {
        bind.invoke(this)
    }
}





