package com.github.insanusmokrassar.utils.ClassExtractor

import com.github.insanusmokrassar.utils.ClassExtractor.exceptions.ClassExtractException

import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

/**
 * Return new instance of target class
 * @param path Path to package as path.to.package.java
 * *
 * @param <T> Target class (or interface)
 * *
 * @return New instance of target class
 * *
 * @throws ClassExtractException
</T> */
@Throws(ClassExtractException::class)
fun <T> extract(path: String, vararg constructorArgs: Any?): T {
    val targetClass = getClass<T>(path)
    targetClass.constructors.forEach {
        if (it.parameterTypes.size != constructorArgs.size) {
            return@forEach
        }
        try {
            return it.newInstance(*constructorArgs) as T
        } catch (e: InstantiationException) {
            throw ClassExtractException("Can't instantiate the instance of class: it may be interface or abstract class", e)
        } catch (e: IllegalAccessException) {
            throw ClassExtractException("Can't instantiate the instance of class: can't get access for instantiating it", e)
        } catch (e: InvocationTargetException) {
            return@forEach
        } catch (e: IllegalArgumentException) {
            return@forEach
        }

    }
    throw ClassExtractException("Can't find constructor for this args")
}

@Throws(IllegalArgumentException::class, IllegalStateException::class)
fun reorderArguments(path: String, vararg constructorArgs: Any?) {
    val targetClass = getClass<Any>(path)
    targetClass.constructors.forEach {
        val leftArgs = arrayListOf(*constructorArgs).map { if (it == null) Any::class.java else it::class.java }
        it.parameterTypes.forEach {
            parameter ->
            leftArgs.forEach {
                currentArg ->
                try {
                    currentArg.asSubclass(parameter)
                } catch (e: ClassCastException) {

                }
            }
        }
    }
}

@Throws(ClassExtractException::class)
fun <T> getClass(path: String): Class<T> {
    try {
        val targetClass = Class.forName(path)
        return targetClass as Class<T>
    } catch (e: ClassNotFoundException) {
        throw ClassExtractException("Can't find class with this classPath: " + path, e)
    }

}
