package insogroup.utils.utils.IOC.strategies.IOCStrategiesWithGenerators;

import insogroup.utils.utils.IOC.IOC;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StandardCacheParamsIOCStrategy implements IOCStrategy {
    protected Map<String, JSONObject> templates = new HashMap<>();

    protected IOCStrategy generator;
    /**
     * @param params
     * Await: <pre>
     *     {
     *         "targetGenerator" : "ioc name or canonical classpath",
     *         "generatorParams" : object,
     *         "templates" : {
     *
     *         }
     *     }
     * </pre>
     */
    public StandardCacheParamsIOCStrategy(JSONObject params) throws ResolveStrategyException {
        String iocName = params.getString("targetGenerator");
        if (params.has("generatorParams")) {
            generator = IOC.resolve(iocName, params.get("generatorParams"));
        } else {
            generator = IOC.resolve(iocName);
        }

        if (params.has("templates")) {
            JSONObject templates = params.getJSONObject("templates");

            for (String key : templates.keySet()) {
                templates.put(key, templates.getJSONObject(key));
            }
        }
    }

    /**
     * Await at least one argument - name of dep
     * @param args Args for using in strategy
     * @return
     * @throws ResolveStrategyException
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {

        try {
            if (args.length > 0) {
                String name = (String) args[0];
                if (templates.containsKey(name)) {
                    return generator.getInstance(templates.get(name));
                } else {
                    JSONObject params;
                    if (args.length > 1) {
                        params = (JSONObject) args[1];
                    } else {
                        params = new JSONObject();
                    }
                    return generator.getInstance(params);
                }
            }
            return generator.getInstance(new JSONObject());
        } catch (ResolveStrategyException e) {
            throw new ResolveStrategyException("Can't generate object with params: " + Arrays.toString(args), e);
        }
    }
}
