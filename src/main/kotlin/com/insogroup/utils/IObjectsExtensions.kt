package com.insogroup.utils

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.iobjectk.realisations.SimpleIObject


fun IObject<Any>.addAll(vararg objects: IObject<Any>) {
    for (current in objects) {
        for (key in current.keys()) {
            put(key, current.get(key))
        }
    }
}

fun IObject<Any>.has(key: String): Boolean {
    return keys().contains(key)
}
