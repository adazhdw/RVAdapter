package com.adazhdw.adapter.core

/**
 * author：daguozhu
 * date-time：2020/12/15 19:19
 * description：
 **/
interface IAdapter<Item> {
    fun getItem(position: Int): Item?
}