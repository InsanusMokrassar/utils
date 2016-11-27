package insogroup.utils.utils.IOC.strategies;

import insogroup.utils.exceptions.ClassExtractException;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import insogroup.utils.utils.IOC.strategies.CacheIOCStrategy;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedExecutorServiceStrategy implements IOCStrategy {

    protected Map<String, ExecutorService> executorServices = new HashMap<>();

    protected Integer defaultThreadsCount = 1;

    /**
     * @param settings
     * <pre>
     *     {
     *         "default" : number,//this number will used for keys which will not set in settings
     *         "taskName" : number,
     *         "taskName2" : number,
     *         "taskName3" : number,
     *         ...
     *     }
     * </pre>
     */
    public FixedExecutorServiceStrategy(JSONObject settings) {
        Set<String> keys = settings.keySet();
        for (String key : keys) {
            if (key.equals("default")) {
                defaultThreadsCount = settings.getInt(key);
                continue;
            }
            executorServices.put(key, Executors.newFixedThreadPool(settings.getInt(key)));
        }
    }

    @Override
    public Object getInstance(Object... objects) throws ResolveStrategyException {
        String targetTaskName = (String) objects[0];
        if (executorServices.containsKey(targetTaskName)) {
            return executorServices.get(targetTaskName);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsCount);
        executorServices.put(targetTaskName, executorService);
        return executorService;
    }
}
