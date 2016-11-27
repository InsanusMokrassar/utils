package insogroup.utils.utils.IOC.interfaces;

import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;

/**
 * Using by IOC for getting instance from strategy
 */
public interface IOCStrategy {
    /**
     * Using by IOC for getting instance from strategy
     * @param args Args for using in strategy
     * @return new, cached or other instance of object
     * @throws ResolveStrategyException Throw when instance can't be resolved
     */
    Object getInstance(Object... args) throws ResolveStrategyException;
}
