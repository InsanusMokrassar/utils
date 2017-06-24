package com.insogroup.utils.IOC

import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException
import com.insogroup.utils.ClassExtractor.ClassExtractor
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException
import com.insogroup.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap

/**
 * IOC container using for resolving some dependencies
 */
class IOC {
    private val strategies: MutableMap<String, IOCStrategy> = HashMap()
    private val subscribers: MutableList<(String) -> Unit> = ArrayList() 

    /**
     * Register dependency in system
     * @param key Using for ident strategy in the future
     * *
     * @param strategy Some strategy
     */
    fun register(key: String, strategy: IOCStrategy) {
        synchronized(strategies) {
            strategies.put(key, strategy)
        }
        subscribers.forEach {
            it("Strategy for key \"$key\" with strategy \"$strategy\" registered.\n")
        }
    }

    fun subscribe(vararg mailBoxes: (String) -> Unit) {
        subscribers.addAll(mailBoxes)
    }

    fun unsubscribe(vararg mailBoxes: (String) -> Unit) {
        subscribers.removeAll(mailBoxes)
    }

    fun subscribe(mailBoxes: List<(String) -> Unit>) {
        subscribers.addAll(mailBoxes)
    }

    fun unsubscribe(mailBoxes: List<(String) -> Unit>) {
        subscribers.removeAll(mailBoxes)
    }

    /**
     * Call for get dependency
     * @param key Key of dependency
     * *
     * @param args Args for dependency
     * *
     * @param <T> Type of target dependency
     * *
     * @return Resolved dependency
     * *
     * @throws ResolveStrategyException Throw when strategy is not registered
    </T> */
    @Throws(ResolveStrategyException::class)
    fun <T: Any> resolve(key: String, vararg args: Any): T {
        try {
            val strategy = strategies[key]
            return strategy!!.getInstance(*args) as T
        } catch (e: NullPointerException) {
            try {
                return ClassExtractor.extract<T>(key, *args)
            } catch (e1: ClassExtractException) {
                throw ResolveStrategyException("Can't find strategy by key or extract class: $key", e)
            }

        }

    }
}
