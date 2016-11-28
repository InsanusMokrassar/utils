package com.insogroup.utils.IOC.strategies;

import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;

public class OneObjectIOCStrategy implements IOCStrategy {

    protected Object targetObject;

    public OneObjectIOCStrategy(Object targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * @param args Args for using in strategy
     * @return Always return the object which was set in constructor
     * @throws ResolveStrategyException
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {
        return targetObject;
    }
}
