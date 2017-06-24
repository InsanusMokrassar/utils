package com.insogroup.utils.IOC.strategies

import com.insogroup.utils.IOC.IOC
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy

/**
 * Simple realisation of redirect IOCStrategy
 * @param targetStrategy Strategy which will used for resolving dependencies
 */
class RedirectIOCStrategy(private val IOC: IOC, private val targetStrategy: String) : IOCStrategy {
    @Throws(ResolveStrategyException::class)
    override fun getInstance(vararg args: Any): Any {
        return IOC.resolve<Any>(targetStrategy, *args)
    }
}
