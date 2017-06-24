package com.insogroup.utils.initialisator.interfaces

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.insogroup.utils.initialisator.exceptions.InitializableException

interface Initializable {
    @Throws(InitializableException::class)
    fun initObject(from: IObject<Any>)
}