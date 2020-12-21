package com.adazhdw.adapter.binding

import androidx.annotation.LayoutRes
import com.adazhdw.adapter.core.ViewHolderDefault

/**
 * author：adazhdw
 * date-time：2020/12/19 10:12
 * description：DataBinding Adpater相关扩展方法
 **/

inline fun <M : Any> defaultBindingItem(
    @LayoutRes layoutRes: Int,
    data: M,
    crossinline bind: ViewHolderDefault.() -> Unit
) = DataBindingItem<M>(layoutRes).apply {
    this.data = data
    onGetViewHolder {
        bind.invoke(this)
    }
}



