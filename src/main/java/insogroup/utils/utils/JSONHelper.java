package insogroup.utils.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONHelper {


    /**
     * Concatenate all JSON objects(last will be concatenate last and him fields will write always)
     * @param objects Input objects
     * @return New generated JSONObject
     */
    public static JSONObject concat(JSONObject... objects){
        JSONObject res = new JSONObject();
        for (JSONObject current : objects){
            for (String key : current.keySet()){
                res.put(key, current.get(key));
            }
        }
        return res;
    }

    public static Object[] toArray(JSONArray jsonArray) {
        Object[] array = new Object[jsonArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = jsonArray.get(i);
        }
        return array;
    }
}
