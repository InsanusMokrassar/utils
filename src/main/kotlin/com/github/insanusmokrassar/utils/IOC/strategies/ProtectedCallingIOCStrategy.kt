package com.github.insanusmokrassar.utils.IOC.strategies

import com.github.insanusmokrassar.iobjectk.interfaces.IObject
import com.github.insanusmokrassar.iobjectk.interfaces.has
import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException
import com.github.insanusmokrassar.utils.ClassExtractor.extract
import com.github.insanusmokrassar.utils.ClassExtractor.getClass
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.exceptions.ResolveStrategyException
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import java.util.ArrayList


class ProtectedCallingIOCStrategy : IOCStrategy {

    protected var e: Throwable? = null

    protected var targetIOCStrategy: IOCStrategy? = null
    protected var requiredAnnotations: MutableList<String>? = null


    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "targetStrategy" : "package.for.target.strategy",
     *         "targetStrategyParams" : SOME PARAMS OBJECT AS OBJECT, STRING, ARRAY AND OTHER,//not required
     *         "requiredAnnotations" : [
     *              "Annotation1",
     *              "Annotation2"
     *              ...
     *         ]
     *     }
     * </pre>
     */
    @Throws(ClassExtractException::class)
    constructor(params: IObject<Any>) {
        var strategyParams: Any = arrayOfNulls<Any>(0)
        val targetIOCStrategyClassPath = params.get<String>(TARGET_IOC_STRATEGY_FIELD)
        if (params.has(TARGET_IOC_STRATEGY_PARAMS_FIELD)) {
            strategyParams = params.get(TARGET_IOC_STRATEGY_PARAMS_FIELD)
        }
        targetIOCStrategy = extract<IOCStrategy>(targetIOCStrategyClassPath, strategyParams)
        val requiredAnnotationsInJSON = params.get<List<Any>>(REQUIRED_ANNOTATIONS_FIELD)
        requiredAnnotations = ArrayList<String>()
        for (currentObject in requiredAnnotationsInJSON) {
            requiredAnnotations!!.add(currentObject.toString())
        }
    }

    /**
     * Called class, which call IOC.resolve must have required annotations
     * @param args Args for using in target strategy
     * *
     * @return instance which will getted from target strategy
     * *
     * @throws ResolveStrategyException
     */
    @Throws(ResolveStrategyException::class)
    override fun <T> getInstance(vararg args: Any): T {
        val iocClass = IOC::class.java.canonicalName
        val stackTraceElements = Thread.currentThread().stackTrace
        var targetClassName: String? = null
        try {
            for (i in stackTraceElements.indices) {
                if (stackTraceElements[i].className == iocClass) {
                    targetClassName = stackTraceElements[i + 1].className
                    break
                }
            }
            val targetClass = getClass<Any>(targetClassName!!)
            val tempReqAnnotations = ArrayList(requiredAnnotations!!)
            targetClass.annotations.forEach {
                if (tempReqAnnotations.contains(it.toString())) {
                    tempReqAnnotations.remove(it.toString())
                }
            }
            if (tempReqAnnotations.isEmpty()) {
                return targetIOCStrategy!!.getInstance(*args)
            }
            throw ResolveStrategyException("Target class haven't required rules: $tempReqAnnotations")
        } catch (e: ClassExtractException) {
            throw ResolveStrategyException("Can't get class of called object: $targetClassName", e)
        } catch (e: IndexOutOfBoundsException) {
            throw ResolveStrategyException("Can't get StackTraceElement of called class: $stackTraceElements", e)
        }

    }

    companion object {

        val TARGET_IOC_STRATEGY_FIELD = "targetStrategy"
        val TARGET_IOC_STRATEGY_PARAMS_FIELD = "targetStrategyParams"
        val REQUIRED_ANNOTATIONS_FIELD = "requiredAnnotations"
    }
}
