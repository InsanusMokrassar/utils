package com.github.insanusmokrassar.utils.initialisator.interfaces

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.initialisator.exceptions.InitializableException

interface Initializable {
    @Throws(InitializableException::class)
    fun initObject(from: IObject<Any>)
}