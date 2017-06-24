package com.insogroup.utils.IOC.interfaces

import com.insogroup.utils.IOC.exceptions.ResolveStrategyException

/**
 * Using by IOC for getting instance from strategy
 */
interface IOCStrategy {
    /**
     * Using by IOC for getting instance from strategy
     * @param args Args for using in strategy
     * *
     * @return new, cached or other instance of object
     * *
     * @throws ResolveStrategyException Throw when instance can't be resolved
     */
    @Throws(ResolveStrategyException::class)
    fun getInstance(vararg args: Any): Any
}
