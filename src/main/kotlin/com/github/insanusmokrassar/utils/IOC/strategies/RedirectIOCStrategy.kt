package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.getOrCreateIOCInstance
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

/**
 * Simple realisation of redirect IOCStrategy
 * @param targetStrategy Strategy which will used for resolving dependencies
 */
class RedirectIOCStrategy(iocName: String, private val targetStrategy: String) : IOCStrategy {

    private val ioc = getOrCreateIOCInstance(iocName)

    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        return ioc.resolve<T>(targetStrategy, *args)
    }
}
