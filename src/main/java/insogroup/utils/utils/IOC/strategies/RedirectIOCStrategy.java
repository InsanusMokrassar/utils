package insogroup.utils.utils.IOC.strategies;

import insogroup.utils.utils.IOC.IOC;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;

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
