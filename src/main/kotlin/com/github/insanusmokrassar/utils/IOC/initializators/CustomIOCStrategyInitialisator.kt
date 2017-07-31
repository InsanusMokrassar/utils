package com.github.insanusmokrassar.utils.IOC.initializators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import com.github.insanusmokrassar.utils.initialisator.exceptions.InitializableException
import com.github.insanusmokrassar.utils.initialisator.interfaces.Initializable

class CustomIOCStrategyInitialisator(private val IOC: IOC): Initializable {

    /**
     * Await:
     * <pre>
     * {
     * "package.to.strategy" : {
     * "NAME OF DEPENDENCY" : STRATEGY PARAMETER (object, array or other value)
     * "NAME OF DEPENDENCY" : STRATEGY PARAMETER (object, array or other value)
     * },
     * OTHER KEYS AND VALUES
     * }
    </pre> *
     * @param from setting for initialisation
     * *
     * @throws InitializableException throw when one of value is not param for strategy or strategy can't be instantiated
     */
    @Throws(InitializableException::class)
    override fun initObject(from: IObject<Any>) {
        val keys = from.keys()
        for (key in keys) {
            try {
                val dependencies = from.get<IObject<Any>>(key)
                val dependenciesKeys = dependencies.keys()
                for (currentDependency in dependenciesKeys) {
                    val currentDependencyParam = dependencies.get<Any>(currentDependency)
                    try {
                        val strategy = extract<IOCStrategy>(key, currentDependencyParam)
                        IOC.register(currentDependency, strategy)
                    } catch (e: ClassExtractException) {
                        throw InitializableException("Can't create IOCStrategy with params: " +
                                currentDependency + "(key) " +
                                currentDependencyParam + "(param)", e)
                    }

                }
            } catch (e: Exception) {
                val dependencies = from.get<List<Any>>(key)
                dependencies.forEach {
                    try {
                        if (it is String) {
                            val strategy = extract<IOCStrategy>(key)
                            IOC.register(it, strategy)
                        }
                    } catch (e1: ClassExtractException) {
                        throw InitializableException("Can't create IOCStrategy with params: $it ", e)
                    }

                }
            }

        }
    }
}
