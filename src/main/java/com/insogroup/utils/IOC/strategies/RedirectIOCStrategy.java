package com.insogroup.utils.IOC.strategies;

import com.insogroup.utils.IOC.IOC;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;

/**
 * Simple realisation of redirect IOCStrategy
 */
public class RedirectIOCStrategy implements IOCStrategy {
    protected String targetStrategy;

    /**
     * @param targetStrategy Strategy which will used for resolving dependencies
     */
    public RedirectIOCStrategy(String targetStrategy) {
        this.targetStrategy = targetStrategy;
    }

    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {
        return IOC.resolve(targetStrategy, args);
    }
}
