package com.github.insanusmokrassar.utils.IOC.strategies.IOCStrategiesWithGenerators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.iobjectk.interfaces.has
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import com.github.insanusmokrassar.utils.IOCField
import com.github.insanusmokrassar.utils.configField
import com.github.insanusmokrassar.utils.presetField
import com.github.insanusmokrassar.utils.strategyField
import java.util.*

class StandardCacheParamsIOCStrategy : IOCStrategy {
    protected var templates: Map<String, IObject<Any>> = HashMap()

    protected var generator: IOCStrategy

    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "strategy" : "ioc name or canonical classpath",
     *         "config" : object,
     *         "preset" : {
     *
     *         }
     *     }
     * </pre>
     */
    @Throws(ResolveStrategyException::class)
    constructor(params: IObject<Any>) {
        val IOC = params.get<IOC>(IOCField)
        val strategyName = params.get<String>(strategyField)
        if (params.keys().contains(configField)) {
            generator = IOC.resolve(strategyName, params.get(configField))
        } else {
            generator = IOC.resolve<IOCStrategy>(strategyName)
        }

        if (params.has(presetField)) {
            val templates = params.get<IObject<Any>>(presetField)

            for (key in templates.keys()) {
                templates.put(key, templates.get<IObject<Any>>(key))
            }
        }
    }

    /**
     * Await at least one argument - name of dep
     * @param args Args for using in strategy
     * *
     * @return
     * *
     * @throws ResolveStrategyException
     */
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {

        try {
            if (args.isNotEmpty()) {
                val name = args[0] as String
                if (templates.containsKey(name)) {
                    return generator.getInstance(templates[name]!!)
                } else {
                    val params: Array<Any>?
                    if (args.size > 1) {
                        params = Arrays.copyOfRange(args, 1, args.size)
                    } else {
                        params = null
                    }
                    if (params == null) {
                        return generator.getInstance()
                    } else {
                        return generator.getInstance(params)
                    }
                }
            }
            return generator.getInstance()
        } catch (e: ResolveStrategyException) {
            throw ResolveStrategyException("Can't generate object with params: $args", e)
        }

    }
}
