package com.insogroup.utils.IOC.strategies.IOCStrategiesWithGenerators;

import com.insogroup.utils.IOC.IOC;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StandardCacheObjectIOCStrategy implements IOCStrategy {
    protected Map<String, Object> knownObjects = new HashMap<>();

    protected IOCStrategy generator;

    /**
     * @param params
     * Await: <pre>
     *     {
     *         "targetGenerator" : "ioc name or canonical classpath",
     *         "generatorParams" : object,
     *         "objectsPreset" : {
     *         }
     *     }
     * </pre>
     */
    public StandardCacheObjectIOCStrategy(JSONObject params) throws ResolveStrategyException{

        String iocName = params.getString("targetGenerator");
        if (params.has("generatorParams")) {
            generator = IOC.resolve(iocName, params.get("generatorParams"));
        } else {
            generator = IOC.resolve(iocName);
        }

        if (params.has("objectsPreset")) {
            JSONObject templates = params.getJSONObject("objectsPreset");

            for (String key : templates.keySet()) {
                knownObjects.put(key, generator.getInstance(templates.getJSONObject(key)));
            }
        }
    }

    /**
     * Await at least one argument - name of dep
     * @param args Args for using in strategy
     * @return Generated instance
     * @throws ResolveStrategyException
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {


        String name = (String) args[0];

        if (knownObjects.containsKey(name)) {
            return knownObjects.get(name);
        }

        JSONObject params;
        if (args.length > 1) {
            params = (JSONObject) args[1];
        } else {
            params = new JSONObject();
        }
        try {
            Object object = generator.getInstance(params);
            knownObjects.put(name, object);
            return object;
        } catch (ResolveStrategyException e) {
            throw new ResolveStrategyException("Can't resolve new object by params", e);
        }
    }
}
