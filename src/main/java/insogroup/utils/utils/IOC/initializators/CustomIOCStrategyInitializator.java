package insogroup.utils.utils.IOC.initializators;

import insogroup.utils.exceptions.ClassExtractException;
import insogroup.utils.utils.initialisator.exceptions.InitializableException;
import insogroup.utils.utils.ClassExtractor;
import insogroup.utils.utils.IOC.IOC;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import insogroup.utils.utils.initialisator.interfaces.Initializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class CustomIOCStrategyInitializator implements Initializable {
    /**
     * Await:
     * <pre>
     *     {
     *         "package.to.strategy" : {
     *                  "NAME OF DEPENDENCY" : STRATEGY PARAMETER (object, array or other value)
     *                  "NAME OF DEPENDENCY" : STRATEGY PARAMETER (object, array or other value)
     *         },
     *         OTHER KEYS AND VALUES
     *     }
     * </pre>
     * @param initJSON setting for initialisation
     * @throws InitializableException throw when one of value is not param for strategy or strategy can't be instantiated
     */
    @Override
    public void init(JSONObject initJSON) throws InitializableException {
        Set<String> keys = initJSON.keySet();
        for (String key : keys) {
            try {
                JSONObject dependencies = initJSON.getJSONObject(key);
                Set<String> dependenciesKeys = dependencies.keySet();
                for (String currentDependency : dependenciesKeys) {
                    Object currentDependencyParam = dependencies.get(currentDependency);
                    try {
                        IOCStrategy strategy = ClassExtractor.extract(key, currentDependencyParam);
                        IOC.register(currentDependency, strategy);
                    } catch (ClassExtractException e) {
                        throw new InitializableException("Can't create IOCStrategy with params: " +
                                currentDependency + "(key) " +
                                currentDependencyParam + "(param)", e);
                    }
                }
            } catch (JSONException e) {
                JSONArray dependencies = initJSON.getJSONArray(key);
                for (int i = 0; i < dependencies.length(); i++) {
                    String dependencyName = dependencies.getString(i);
                    try {
                        IOCStrategy strategy = ClassExtractor.extract(key);
                        IOC.register(dependencyName, strategy);
                    } catch (ClassExtractException e1) {
                        throw new InitializableException("Can't create IOCStrategy with params: " +
                                dependencyName + "(key) ", e);
                    }
                }
            }
        }
    }
}
