package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.ClassExtractor.getClass
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

class SimpleIOCStrategy(protected var classPath: String) : IOCStrategy {


    /**
     * @param classPath package.name for using with @[ClassExtractor] in the future
     */
    init {
        getClass<Any>(classPath)
    }

    /**
     * @param args Args for using in strategy
     * *
     * @return new instance
     * *
     * @throws ResolveStrategyException Throw when object with this classPath and args can't be instantiated
     */
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        try {
            val instance = extract<T>(classPath, *args)
            return instance
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't resolve instance with this classPath or args", e)
        }

    }
}
