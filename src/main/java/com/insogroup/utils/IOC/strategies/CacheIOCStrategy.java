package com.insogroup.utils.IOC.strategies;

import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException;
import com.insogroup.utils.ClassExtractor.ClassExtractor;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;

import java.util.HashMap;
import java.util.Map;

public class CacheIOCStrategy implements IOCStrategy {
    protected String targetClassPath;

    protected Map<String, Object> instances = new HashMap<>();

    /**
    * @param targetClassPath package.name for using with @{@link ClassExtractor}
    */
    public CacheIOCStrategy(String targetClassPath) throws ClassExtractException {
        this.targetClassPath = targetClassPath;
    }

    /**
     * Return cached instance of object
     * @param args not used
     * @return Instance that was generated in constructor
     * @throws ResolveStrategyException Throw when in constructor was not created object
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {
        if (args.length == 0) {
            return getInstance("");
        }
        String name = (String) args[0];
        if (instances.containsKey(name)) {
            return instances.get(name);
        }
        Object[] constructorArgs = new Object[args.length-1];//first argument is classpath
        System.arraycopy(args, 1, constructorArgs, 0, constructorArgs.length);
        try {
            Object instance = ClassExtractor.extract(targetClassPath, constructorArgs);
            instances.put(name, instance);
            return instance;
        } catch (ClassExtractException e) {
            throw new ResolveStrategyException("Can't find variable for this name and create new instance", e);
        }
    }
}
