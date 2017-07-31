package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

/**
 * Simple realisation of redirect IOCStrategy
 * @param targetStrategy Strategy which will used for resolving dependencies
 */
class RedirectIOCStrategy(private val IOC: IOC, private val targetStrategy: String) : IOCStrategy {
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        return IOC.resolve<T>(targetStrategy, *args)
    }
}
