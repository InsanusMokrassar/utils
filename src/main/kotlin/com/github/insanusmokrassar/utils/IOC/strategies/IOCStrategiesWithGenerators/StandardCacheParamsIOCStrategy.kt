package com.github.insanusmokrassar.utils.IOC.strategies.IOCStrategiesWithGenerators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.iobjectk.interfaces.has
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import java.util.*

class StandardCacheParamsIOCStrategy : IOCStrategy {
    protected var templates: Map<String, IObject<Any>> = HashMap<String, IObject<Any>>()

    protected var generator: IOCStrategy
    protected val IOC: IOC

    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "targetGenerator" : "ioc name or canonical classpath",
     *         "generatorParams" : object,
     *         "templates" : {
     *
     *         }
     *     }
     * </pre>
     */
    @Throws(ResolveStrategyException::class)
    constructor(IOC: IOC, params: IObject<Any>) {
        this.IOC = IOC
        val iocName = params.get<String>("targetGenerator")
        if (params.keys().contains("generatorParams")) {
            generator = IOC.resolve(iocName, params.get("generatorParams"))
        } else {
            generator = IOC.resolve<IOCStrategy>(iocName)
        }

        if (params.has("templates")) {
            val templates = params.get<IObject<Any>>("templates")

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
