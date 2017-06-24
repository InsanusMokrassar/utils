package com.insogroup.utils.IOC.strategies

import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy

class OneObjectIOCStrategy(protected var targetObject: Any) : IOCStrategy {

    /**
     * @param args Args for using in strategy
     * *
     * @return Always return the object which was set in constructor
     * *
     * @throws ResolveStrategyException
     */
    @Throws(ResolveStrategyException::class)
    override fun getInstance(vararg args: Any): Any {
        return targetObject
    }
}
