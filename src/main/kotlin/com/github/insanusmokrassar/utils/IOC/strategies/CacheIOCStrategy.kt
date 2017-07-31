package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap

class CacheIOCStrategy(protected var targetClassPath: String) : IOCStrategy {

    protected var instances: MutableMap<String, Any> = HashMap()

    /**
     * Return cached instance of object
     * @param args not used
     * *
     * @return Instance that was generated in constructor
     * *
     * @throws ResolveStrategyException Throw when in constructor was not created object
     */
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        if (args.size == 0) {
            return getInstance("")
        }
        val name = args[0] as String
        if (instances.containsKey(name)) {
            return instances[name]!! as T
        }
        val constructorArgs = arrayOfNulls<Any>(args.size - 1)//first argument is classpath
        System.arraycopy(args, 1, constructorArgs, 0, constructorArgs.size)
        try {
            val instance = extract<Any>(targetClassPath, constructorArgs)
            instances.put(name, instance)
            return instance as T
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't find variable for this name and create new instance", e)
        }

    }
}
