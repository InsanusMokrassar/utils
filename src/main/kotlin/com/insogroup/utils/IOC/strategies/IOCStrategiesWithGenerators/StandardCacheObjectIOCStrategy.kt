package com.insogroup.utils.IOC.strategies.IOCStrategiesWithGenerators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.insogroup.utils.IOC.IOC
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy
import java.util.*

class StandardCacheObjectIOCStrategy : IOCStrategy {
    protected var knownObjects: MutableMap<String, Any> = HashMap()

    protected var generator: IOCStrategy
    protected val IOC: IOC

    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "targetGenerator" : "ioc name or canonical classpath",
     *         "generatorParams" : object,//optional, if object need some args to create
     *         "objectsPreset" : {
     *         }
     *     }
     * </pre>
     */
    constructor(IOC: IOC, params: IObject<Any>) {
        this.IOC = IOC
        val iocName = params.get<String>("targetGenerator")
        if (params.keys().contains("generatorParams")) {
            generator = IOC.resolve(iocName, params.get("generatorParams"))
        } else {
            generator = IOC.resolve<IOCStrategy>(iocName)
        }

        if (params.keys().contains("objectsPreset")) {
            val templates = params.get<IObject<Any>>("objectsPreset")

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
    override fun getInstance(vararg args: Any): Any {


        val name = args[0] as String

        if (knownObjects.containsKey(name)) {
            return knownObjects[name]!!
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
            return result
        } catch (e: ResolveStrategyException) {
            throw ResolveStrategyException("Can't resolve new object by params", e)
        }

    }
}
