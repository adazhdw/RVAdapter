package com.adazhdw.adapter.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView

/**
 * author：adazhdw
 * date-time：2020/12/18 15:42
 * description：
 **/
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val mContext: Context = itemView.context
    private val views: SparseArray<View> = SparseArray()

    fun <T : View> getView(@IdRes viewId: Int): T {
        val view: T? = getViewOrNull(viewId)
        checkNotNull(view) { "No view found with id:$viewId" }
        return view
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getViewOrNull(@IdRes viewId: Int): T? {
        val view = views.get(viewId)
        if (view == null) {
            itemView.findViewById<T>(viewId)?.let {
                views.put(viewId, it)
                return it
            }
        }
        return view as? T
    }

    open fun setText(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
        getView<TextView>(viewId).text = value
        return this
    }

    open fun setText(@IdRes viewId: Int, @StringRes strId: Int): BaseViewHolder {
        getView<TextView>(viewId).setText(strId)
        return this
    }

    open fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        getView<TextView>(viewId).setTextColor(color)
        return this
    }

    open fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int): BaseViewHolder {
        getView<TextView>(viewId).setTextColor(itemView.resources.getColor(colorRes))
        return this
    }

    open fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): BaseViewHolder {
        getView<ImageView>(viewId).setImageResource(imageResId)
        return this
    }

    open fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): BaseViewHolder {
        getView<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    open fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): BaseViewHolder {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    open fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        getView<View>(viewId).setBackgroundColor(color)
        return this
    }

    open fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    open fun setVisible(@IdRes viewId: Int, isVisible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        return this
    }

    open fun setGone(@IdRes viewId: Int, isGone: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (isGone) View.GONE else View.VISIBLE
        return this
    }

    open fun setVisible(@IdRes viewId: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = View.VISIBLE
        return this
    }

    open fun setInvisible(@IdRes viewId: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = View.INVISIBLE
        return this
    }

    open fun setGone(@IdRes viewId: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = View.GONE
        return this
    }

    open fun setEnabled(@IdRes viewId: Int, isEnabled: Boolean): BaseViewHolder {
        getView<View>(viewId).isEnabled = isEnabled
        return this
    }

}