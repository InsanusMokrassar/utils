package com.insogroup.utils.IOC.strategies;

import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException;
import com.insogroup.utils.ClassExtractor.ClassExtractor;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;

public class SimpleIOCStrategy implements IOCStrategy {

    protected String classPath;

    /**
     * @param classPath package.name for using with @{@link ClassExtractor} in the future
     */
    public SimpleIOCStrategy(String classPath) throws ClassExtractException {
        ClassExtractor.getClass(classPath);
        this.classPath = classPath;
    }

    /**
     * @param args Args for using in strategy
     * @return new instance
     * @throws ResolveStrategyException Throw when object with this classPath and args can't be instantiated
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {
        try {
            Object instance = ClassExtractor.extract(classPath, args);
            return instance;
        } catch (ClassExtractException e) {
            throw new ResolveStrategyException("Can't resolve instance with this classPath or args", e);
        }
    }
}