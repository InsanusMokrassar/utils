package com.insogroup.utils.initialisator.interfaces;

import com.insogroup.utils.initialisator.exceptions.InitializableException;
import org.json.JSONObject;

/**
 * Objects which implement (or extend) this interface must be initialized by calling method @Initializable.init
 */
public interface Initializable {
    /**
     * Init the object
     * @param initJSON setting for initialisation
     * @throws InitializableException Throws when object can't be initialised with current input json
     */
    void init(JSONObject initJSON) throws InitializableException;
}
