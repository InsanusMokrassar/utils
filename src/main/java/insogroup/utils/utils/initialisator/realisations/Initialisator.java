package insogroup.utils.utils.initialisator.realisations;

import insogroup.utils.exceptions.ClassExtractException;
import insogroup.utils.utils.initialisator.exceptions.InitializableException;
import insogroup.utils.utils.ClassExtractor;
import insogroup.utils.utils.initialisator.interfaces.Initializable;
import org.json.JSONObject;

public class Initialisator implements Initializable {

    public static void staticInit(JSONObject initJSON) throws InitializableException {
        new Initialisator().init(initJSON);
    }

    /**
     * Await:
     * <pre>
     *     {
     *         "path.to.class.package" : {
     *              OTHER PARAMS WHICH WOULD BE APPLIED TO THE TARGET CLASS
     *         },
     *
     *     }
     * </pre>
     * You must remember - "path.to.class.package" must implement @{@link Initializable}
     * @param initJSON setting for initialisation
     * @throws InitializableException Throw when initializator can't extract some of initializators
     */
    @Override
    public void init(JSONObject initJSON) throws InitializableException {
        try {
            for (String key : initJSON.keySet()) {
                Initializable targetInitializator = ClassExtractor.<Initializable>extract(key);
                targetInitializator.init(initJSON.getJSONObject(key));
            }
        } catch (ClassExtractException e) {
            throw new InitializableException("Can't extract some of initializators", e);
        }
    }
}
