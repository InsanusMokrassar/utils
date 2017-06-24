package com.insogroup.utils.IOC.initializators

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.insogroup.utils.ClassExtractor.ClassExtractor
import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException
import com.insogroup.utils.IOC.IOC
import com.insogroup.utils.IOC.interfaces.IOCStrategy
import com.insogroup.utils.initialisator.exceptions.InitializableException
import com.insogroup.utils.initialisator.interfaces.Initializable

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
                        val strategy = ClassExtractor.extract<IOCStrategy>(key, currentDependencyParam)
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
                            val strategy = ClassExtractor.extract<IOCStrategy>(key)
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
