package com.adazhdw.adapter.core

import android.util.SparseArray

/**
 * author：adazhdw
 * date-time：2020/12/18 13:56
 * description：
 **/

/**
 * The default implementation to cache the viewholder factories.
 */
class DefaultItemVHFactoryCache<ItemVHFactory : GenericItemVHFactory> : ItemVHFactoryCache<ItemVHFactory> {

    // we remember all possible types so we can create a new view efficiently
    private val typeInstances = SparseArray<ItemVHFactory>()

    override fun register(type: Int, item: ItemVHFactory): Boolean {
        if (typeInstances.indexOfKey(type) < 0) {
            typeInstances.put(type, item)
            return true
        }
        return false
    }

    override fun get(type: Int): ItemVHFactory {
        return typeInstances.get(type)
    }

    override fun contains(type: Int): Boolean = typeInstances.indexOfKey(type) >= 0

    override fun clear() {
        typeInstances.clear()
    }
}
