package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap

class CacheIOCStrategy() : IOCStrategy {

    protected var instances: MutableMap<String, Any> = HashMap()

    /**
     * If as config set object, strategy will await next:
     * <pre>
     *     {
     *          "NAME OF DEPENDENCY" : {
     *              "package"
     *          }
     *     }
     * </pre>
     */
    constructor(preset: IObject<Any>): this() {
        preset.keys().forEach {

        }
    }

    /**
     * Return cached instance of object
     * @param args args[0] - name of dependency, args[1] - package or IOC dependency name, args[2..size] - args for dependency init
     *
     * @return Instance that was generated in constructor
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
            val instance = IOC.resolve<T>(targetClassPath, *constructorArgs)
            instances.put(name, instance)
            return instance
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't find variable for this name and create new instance", e)
        }

    }
}
