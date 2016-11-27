package insogroup.utils.utils.IOC.strategies;

import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;

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
