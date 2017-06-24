package com.insogroup.utils.ClassExtractor

import com.insogroup.utils.ClassExtractor.exceptions.*

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

object ClassExtractor {

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
    fun <T: Any> extract(path: String, vararg constructorArgs: Any): T {
        val targetClass = getClass<T>(path)
        targetClass.constructors.forEach {
            if (it.parameters.size != constructorArgs.size) {
                return@forEach
            }
            try {
                return it.call(*constructorArgs)
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

    @Throws(ClassExtractException::class)
    fun <T: Any> getClass(path: String): KClass<T> {
        try {
            val targetClass = Class.forName(path).kotlin
            return targetClass as KClass<T>
        } catch (e: ClassNotFoundException) {
            throw ClassExtractException("Can't find class with this classPath: " + path, e)
        }

    }
}
