package com.insogroup.utils.IOC.strategies

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FixedExecutorServiceStrategy : IOCStrategy {

    protected var executorServices: MutableMap<String, ExecutorService> = HashMap()

    protected var defaultThreadsCount: Int? = 1

    /**
     * @param settings
     * <pre>
     *     {
     *         "default" : number,//this number will used for keys which will not set in settings
     *         "taskName" : number,
     *         "taskName2" : number,
     *         "taskName3" : number,
     *         ...
     *     }
     * </pre>
     */
    constructor(settings: IObject<Any>) {
        val keys = settings.keys()
        for (key in keys) {
            if (key == "default") {
                defaultThreadsCount = settings.get<Int>(key)
                continue
            }
            executorServices.put(key, Executors.newFixedThreadPool(settings.get<Int>(key)))
        }
    }

    @Throws(ResolveStrategyException::class)
    override fun getInstance(vararg args: Any): Any {
        val targetTaskName = args[0] as String
        if (executorServices.containsKey(targetTaskName)) {
            return executorServices[targetTaskName]!!
        }
        val executorService = Executors.newFixedThreadPool(defaultThreadsCount!!)
        executorServices.put(targetTaskName, executorService)
        return executorService
    }
}
