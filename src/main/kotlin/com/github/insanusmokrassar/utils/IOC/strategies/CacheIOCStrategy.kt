package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.*
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.getOrCreateIOCInstance
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy

import java.util.HashMap

class CacheIOCStrategy(config: IObject<Any>) : IOCStrategy {

    protected var instances: MutableMap<String, Any> = HashMap()
    protected val ioc: IOC = getOrCreateIOCInstance(config.get(IOCNameField))

    /**
     * Await:
     * <pre>
     *     {
     *          "IOCName": "ioc name",
     *          "preset": [
     *              {
     *                  "name": "NAME OF FIRST PRESET",
     *                  "package": "CLASS PATH",
     *                  "args": [
     *                      ARGS,
     *                      OF,
     *                      FIRST,
     *                      PRESET
     *                  ]
     *              }
     *          ]
     *     }
     * </pre>
     */
    init {
        if (config.keys().contains(presetField)) {
            val presetObject = config.get<List<IObject<Any>>>(presetField)
            presetObject.forEach {
                val args: Array<Any>
                if (it.keys().contains(argsField)) {
                    args = it.get<List<Any>>(argsField).toTypedArray()
                } else {
                    args = Array(0, {})
                }
                getInstance(
                        it.get<String>(nameField),
                        it.get<String>(packageField),
                        *args
                )
//                getInstance(it, *currentPresetArgs.toTypedArray())
            }
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
        if (args.isEmpty()) {
            return getInstance("")
        }
        val name = args[0] as String
        if (instances.containsKey(name)) {
            return instances[name]!! as T
        }
        val targetClassPath = args[1] as String
        val constructorArgs = args.sliceArray(2..args.size-1)
        try {
            val instance = ioc.resolve<T>(targetClassPath, *constructorArgs)
            instances.put(name, instance!!)
            return instance
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't find variable for this name and create new instance", e)
        }

    }
}
