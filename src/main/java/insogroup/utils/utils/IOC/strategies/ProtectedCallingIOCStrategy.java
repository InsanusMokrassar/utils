package insogroup.utils.utils.IOC.strategies;

import insogroup.utils.exceptions.ClassExtractException;
import insogroup.utils.utils.ClassExtractor;
import insogroup.utils.utils.IOC.IOC;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProtectedCallingIOCStrategy implements IOCStrategy {

    public static final String TARGET_IOC_STRATEGY_FIELD = "targetStrategy";
    public static final String TARGET_IOC_STRATEGY_PARAMS_FIELD = "targetStrategyParams";
    public static final String REQUIRED_ANNOTATIONS_FIELD = "requiredAnnotations";

    protected Throwable e = null;

    protected IOCStrategy targetIOCStrategy = null;
    protected List<String> requiredAnnotations = null;

    /**
     * @param params
     * Await:
     * <pre>
     *     {
     *         "targetStrategy" : "package.for.target.strategy",
     *         "targetStrategyParams" : SOME PARAMS OBJECT AS OBJECT, STRING, ARRAY AND OTHER,//not required
     *         "requiredAnnotations" : [
     *              "Annotation1",
     *              "Annotation2"
     *              ...
     *         ]
     *     }
     * </pre>
     */
    public ProtectedCallingIOCStrategy(JSONObject params) throws ClassExtractException {
        Object strategyParams = new Object[0];
        String targetIOCStrategyClassPath = params.getString(TARGET_IOC_STRATEGY_FIELD);
        if (params.has(TARGET_IOC_STRATEGY_PARAMS_FIELD)) {
            strategyParams = params.get(TARGET_IOC_STRATEGY_PARAMS_FIELD);
        }
        targetIOCStrategy = ClassExtractor.extract(targetIOCStrategyClassPath, strategyParams);
        JSONArray requiredAnnotationsInJSON = params.getJSONArray(REQUIRED_ANNOTATIONS_FIELD);
        requiredAnnotations = new ArrayList<>();
        for (Object currentObject : requiredAnnotationsInJSON) {
            requiredAnnotations.add(String.valueOf(currentObject));
        }
    }

    /**
     * Called class, which call IOC.resolve must have required annotations
     * @param args Args for using in target strategy
     * @return instance which will getted from target strategy
     * @throws ResolveStrategyException
     */
    @Override
    public Object getInstance(Object... args) throws ResolveStrategyException {
        String iocClass = IOC.class.getCanonicalName();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String targetClassName = null;
        try {
            for (int i = 0; i < stackTraceElements.length; i++) {
                if (stackTraceElements[i].getClassName().equals(iocClass)) {
                    targetClassName = stackTraceElements[i + 1].getClassName();
                    break;
                }
            }
            Class targetClass = ClassExtractor.getClass(targetClassName);
            List<String> tempReqAnnotations = new ArrayList<>(requiredAnnotations);
            for (Annotation currentAnnotation : targetClass.getAnnotations()) {
                if (tempReqAnnotations.contains(currentAnnotation.toString())) {
                    tempReqAnnotations.remove(currentAnnotation.toString());
                }
            }
            if (tempReqAnnotations.isEmpty()) {
                return targetIOCStrategy.getInstance(args);
            }
            throw new ResolveStrategyException("Target class haven't required rules: " + tempReqAnnotations.toString(), null);
        } catch (ClassExtractException e) {
            throw new ResolveStrategyException("Can't get class of called object: " + targetClassName, e);
        } catch (IndexOutOfBoundsException e) {
            throw new ResolveStrategyException("Can't get StackTraceElement of called class: " + Arrays.toString(stackTraceElements), e);
        }
    }
}
