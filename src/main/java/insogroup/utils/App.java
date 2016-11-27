package insogroup.utils;

import insogroup.utils.utils.queues.ArrayQueue;
import insogroup.utils.utils.ClassExtractor;
import insogroup.utils.utils.IOC.IOC;
import insogroup.utils.utils.IOC.exceptions.ResolveStrategyException;
import insogroup.utils.utils.IOC.interfaces.IOCStrategy;
import insogroup.utils.utils.MailBoxes.interfaces.MailBox;
import insogroup.utils.utils.initialisator.realisations.Initialisator;
import insogroup.utils.utils.MailBoxes.OutputStreamMailBox;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hello world!
 *
 */
@App.Example
public class App 
{
    private static final String exampleOfCustomStrategyInitializerConfig = "{\n" +
            "   \"insogroup.utils.utils.IOC.initializators.CustomIOCStrategyInitializator\":{\n" +
            "       \"insogroup.utils.utils.IOC.strategies.SimpleIOCStrategy\" : {\n" +
            "           \"ArrayQueue\" : \"insogroup.utils.utils.queues.ArrayQueue\"\n" +
            "       },\n" +
            "       \"insogroup.utils.utils.IOC.strategies.CacheIOCStrategy\" : {\n" +
            "           \"ArrayQueueCached\" : \"insogroup.utils.utils.queues.ArrayQueue\"\n" +
            "       },\n" +
            "       \"insogroup.utils.utils.IOC.strategies.ProtectedCallingIOCStrategy\" : {\n" +
            "           \"ArrayQueueCachedProtected\" : {" +
            "               \"targetStrategy\" : \"insogroup.utils.utils.IOC.strategies.CacheIOCStrategy\"," +
            "               \"targetStrategyParams\" : \"insogroup.utils.utils.queues.ArrayQueue\"," +
            "               \"requiredAnnotations\" : [" +
            "                   \"@insogroup.utils.App$Example()\"" +
            "               ]" +
            "           }\n" +
            "       }\n" +
            "   }\n" +
            "}\n";

    /**
     * Examples
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException {

        //-------example of MailBox
        OutputStreamMailBox outputStreamMailBox = new OutputStreamMailBox();

        outputStreamMailBox.receive("OutputStreamMailBoxExample Пример 1234\n");

        IOC.subscribe(outputStreamMailBox);

        //------example of IOC
        IOC.register(MailBox.class.toString(), new IOCStrategy() {
            @Override
            public Object getInstance(Object... args) throws ResolveStrategyException{
                if (args.length == 0) {
                    return new OutputStreamMailBox();
                }
                if (args[0].getClass().equals(OutputStream.class)) {
                    return new OutputStreamMailBox((OutputStream) args[0]);
                }
                throw new ResolveStrategyException("Can't return instance with this params", null);
            }
        });
        MailBox exampleSimpleResolvedByIOCMailBox = IOC.resolve(MailBox.class.toString());

        exampleSimpleResolvedByIOCMailBox.receive("example\n");

        //-------example of ClassExtractor
        MailBox<String> extractedMailBox = ClassExtractor.<MailBox<String>>extract(
                OutputStreamMailBox.class.getCanonicalName(),
                System.out);

        //------example of CustomIOCStrategyInitializator
        Initialisator.staticInit(new JSONObject(exampleOfCustomStrategyInitializerConfig));
        //------example of SimpleIOCStrategy
        ArrayQueue arrayQueueFirst = IOC.resolve("ArrayQueue");
        arrayQueueFirst.offer(exampleSimpleResolvedByIOCMailBox);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueFirst.toString() + "\n");

        ArrayQueue arrayQueueSecond = IOC.resolve("ArrayQueue");
        arrayQueueSecond.offer(System.out);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueSecond.toString() + "\n");
        //------example of CacheIOCStrategy
        ArrayQueue arrayQueueCachedFirst = IOC.resolve("ArrayQueueCached");
        arrayQueueCachedFirst.offer(exampleSimpleResolvedByIOCMailBox);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueCachedFirst.toString() + "\n");

        ArrayQueue arrayQueueCachedSecond = IOC.resolve("ArrayQueueCached");
        arrayQueueCachedSecond.offer(System.out);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueCachedSecond.toString() + "\n");
        //------example of ProtectedCallingIOCStrategy using CacheIOCStrategy
        ArrayQueue arrayQueueCachedProtectedFirst = IOC.resolve("ArrayQueueCachedProtected");
        arrayQueueCachedProtectedFirst.offer(exampleSimpleResolvedByIOCMailBox);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueCachedProtectedFirst.toString() + "\n");

        ArrayQueue arrayQueueCachedProtectedSecond = IOC.resolve("ArrayQueueCachedProtected");
        arrayQueueCachedProtectedSecond.offer(System.out);
        exampleSimpleResolvedByIOCMailBox.receive(arrayQueueCachedProtectedSecond.toString() + "\n");

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Example {

    }
}
