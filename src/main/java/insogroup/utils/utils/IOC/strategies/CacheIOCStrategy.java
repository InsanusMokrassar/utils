package insogroup.utils.utils.IOC.strategies;

import insogroup.utils.exceptions.ClassExtractException;
import insogroup.utils.utils.ClassExtractor;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import org.json.JSONArray;

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
