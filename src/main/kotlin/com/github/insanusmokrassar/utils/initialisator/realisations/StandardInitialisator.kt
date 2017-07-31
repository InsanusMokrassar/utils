package com.github.insanusmokrassar.utils.initialisator.realisations

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.utils.ClassExtractor.ClassExtractor
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.initialisator.exceptions.InitializableException
import com.github.insanusmokrassar.utils.initialisator.interfaces.Initializable

@Throws(InitializableException::class)
fun staticInit(initJSON: IObject<Any>) {
    StandardInitialisator().initObject(initJSON)
}

class StandardInitialisator: Initializable {


    /**
     * Await:
     * <pre>
     * {
     * "path.to.class.package" : {
     * OTHER PARAMS WHICH WOULD BE APPLIED TO THE TARGET CLASS
     * },

     * }
    </pre> *
     * You must remember - "path.to.class.package" must implement @[Initializable]
     * @param from setting for initialisation
     * *
     * @throws InitializableException Throw when initializator can't extract some of initializators
     */
    @Throws(InitializableException::class)
    override fun initObject(from: IObject<Any>) {
        try {
            from.keys().forEach {
                val targetInitializator = ClassExtractor.extract<Initializable>(it)
                targetInitializator.initObject(from.get(it))
            }
        } catch (e: ClassExtractException) {
            throw InitializableException("Can't extract some of initializators", e)
        }

    }
}