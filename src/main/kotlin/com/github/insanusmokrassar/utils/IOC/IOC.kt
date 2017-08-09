package com.github.insanusmokrassar.utils.IOC

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.*
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap

private val iocInstances = HashMap<String, IOC>()

/**
 * <pre>
 *     {
 *          "strategies": [
 *              {
 *                  "name": "NameOfStrategy",
 *                  "package": "class.path.to.class.of.strategy",
 *                  "config": any //optional, can be a list (will used as vararg of params), object or static value
 *              }
 *          ]
 *     }
 * </pre>
 */
@Throws(IllegalArgumentException::class)
fun loadIOCConfig(config: IObject<Any>, name: String = defaultField): IOC {
    val into = getOrCreateIOCInstance(name)
    val strategiesList = config.get<List<IObject<Any>>>(strategiesField)
    strategiesList.forEach {
        val args: Array<Any>
        if (it.keys().contains(configField)) {
            args = try {
                it.get<List<Any>>(configField).toTypedArray()
            } catch (e: ClassCastException) {
                arrayOf(it.get<Any>(configField))
            }
        } else {
            args = arrayOf()
        }
        into.register(
                it.get(nameField),
                into.resolve(
                        it.get(packageField),
                        *args
                )
        )
    }
    return into
}

fun getOrCreateIOCInstance(name: String): IOC {
    if (!iocInstances.containsKey(name)) {
        iocInstances.put(name, IOC())
    }
    return iocInstances[name]!!
}

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
    fun <T> resolve(key: String, vararg args: Any): T {
        try {
            val strategy = strategies[key]
            return strategy!!.getInstance<T>(*args)
        } catch (e: NullPointerException) {
            try {
                return extract(key, *args)
            } catch (e1: ClassExtractException) {
                throw ResolveStrategyException("Can't find strategy by key or extract class: $key", e)
            }
        }
    }
}
