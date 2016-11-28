package com.insogroup.utils.ClassExtractor;

import com.insogroup.utils.ClassExtractor.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassExtractor {

    /**
     * Return new instance of target class
     * @param path Path to package as path.to.package.java
     * @param <T> Target class (or interface)
     * @return New instance of target class
     * @throws ClassExtractException
     */
    public static <T> T extract(String path, Object... constructorArgs) throws ClassExtractException {
        Class targetClass = getClass(path);
        Constructor<T>[] constructors = targetClass.getConstructors();
        for (Constructor<T> constructor : constructors) {
            if (constructor.getParameterTypes().length != constructorArgs.length){
                continue;
            }
            try {
                return constructor.newInstance(constructorArgs);
            } catch (InstantiationException e) {
                throw new ClassExtractException("Can't instantiate the instance of class: it may be interface or abstract class", e);
            } catch (IllegalAccessException e) {
                throw new ClassExtractException("Can't instantiate the instance of class: can't get access for instantiating it", e);
            } catch (InvocationTargetException | IllegalArgumentException e) {
                continue;
            }
        }
        throw new ClassExtractException("Can't find constructor for this args");
    }

    public static <T> Class<T> getClass(String path) throws ClassExtractException {
        try {
            Class targetClass = Class.forName(path);
            return (Class<T>) targetClass;
        } catch (ClassNotFoundException e) {
            throw new ClassExtractException("Can't find class with this classPath: " + path, e);
        }
    }
}
