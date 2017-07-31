package com.github.insanusmokrassar.utils.IOC.strategies.IOCStrategiesWithGenerators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import com.github.insanusmokrassar.utils.IOCField
import com.github.insanusmokrassar.utils.configField
import com.github.insanusmokrassar.utils.presetField
import com.github.insanusmokrassar.utils.strategyField
import java.util.*

class StandardCacheObjectIOCStrategy : IOCStrategy {
    protected var knownObjects: MutableMap<String, Any> = HashMap()

    protected var generator: IOCStrategy

    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "strategy" : "ioc name or canonical classpath",
     *         "config" : object,//optional, if object need some args to create
     *         "preset" : {
     *         }
     *     }
     * </pre>
     */
    constructor(params: IObject<Any>) {
        val IOC = params.get<IOC>(IOCField)
        val strategyName = params.get<String>(strategyField)
        if (params.keys().contains(configField)) {
            generator = IOC.resolve(strategyName, params.get(configField))
        } else {
            generator = IOC.resolve<IOCStrategy>(strategyName)
        }

        if (params.keys().contains(presetField)) {
            val templates = params.get<IObject<Any>>(presetField)

            for (key in templates.keys()) {
                knownObjects.put(key, generator.getInstance(templates.get<IObject<Any>>(key)))
            }
        }

    }

    /**
     * Await at least one argument - name of dep
     * @param args Args for using in strategy
     * *
     * @return Generated instance
     * *
     * @throws ResolveStrategyException
     */
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        val name = args[0] as String

        if (knownObjects.containsKey(name)) {
            return knownObjects[name]!! as T
        }

        val params: Array<Any>?
        if (args.size > 1) {
            params = Arrays.copyOfRange(args, 1, args.size)
        } else {
            params = null
        }
        try {
            val result: Any
            if (params == null) {
                result = generator.getInstance()
            } else {
                result = generator.getInstance(params)
            }
            knownObjects.put(name, result)
            return result as T
        } catch (e: ResolveStrategyException) {
            throw ResolveStrategyException("Can't resolve new object by params", e)
        }

    }
}
