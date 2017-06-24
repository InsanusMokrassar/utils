package com.insogroup.utils.IOC.strategies

import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException
import com.insogroup.utils.ClassExtractor.ClassExtractor
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy

class SimpleIOCStrategy(protected var classPath: String) : IOCStrategy {


    /**
     * @param classPath package.name for using with @[ClassExtractor] in the future
     */
    init {
        ClassExtractor.getClass<Any>(classPath)
    }

    /**
     * @param args Args for using in strategy
     * *
     * @return new instance
     * *
     * @throws ResolveStrategyException Throw when object with this classPath and args can't be instantiated
     */
    @Throws(ResolveStrategyException::class)
    override fun getInstance(vararg args: Any): Any {
        try {
            val instance = ClassExtractor.extract<Any>(classPath, *args)
            return instance
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't resolve instance with this classPath or args", e)
        }

    }
}
