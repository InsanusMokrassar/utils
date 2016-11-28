package com.insogroup.utils;

import com.insogroup.utils.queues.ArrayQueue;
import com.insogroup.utils.ClassExtractor.ClassExtractor;
import com.insogroup.utils.IOC.IOC;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;
import com.insogroup.utils.MailBoxes.interfaces.MailBox;
import com.insogroup.utils.initialisator.realisations.Initialisator;
import com.insogroup.utils.MailBoxes.OutputStreamMailBox;
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
@Example.ExampleAnnotation
public class Example
{
    private static final String exampleOfCustomStrategyInitializerConfig = "{\n" +
            "   \"com.insogroup.utils.IOC.initializators.CustomIOCStrategyInitializator\":{\n" +
            "       \"com.insogroup.utils.IOC.strategies.SimpleIOCStrategy\" : {\n" +
            "           \"ArrayQueue\" : \"com.insogroup.utils.queues.ArrayQueue\"\n" +
            "       },\n" +
            "       \"com.insogroup.utils.IOC.strategies.CacheIOCStrategy\" : {\n" +
            "           \"ArrayQueueCached\" : \"com.insogroup.utils.queues.ArrayQueue\"\n" +
            "       },\n" +
            "       \"com.insogroup.utils.IOC.strategies.ProtectedCallingIOCStrategy\" : {\n" +
            "           \"ArrayQueueCachedProtected\" : {" +
            "               \"targetStrategy\" : \"com.insogroup.utils.IOC.strategies.CacheIOCStrategy\"," +
            "               \"targetStrategyParams\" : \"com.insogroup.utils.queues.ArrayQueue\"," +
            "               \"requiredAnnotations\" : [" +
            "                   \"@com.insogroup.utils.Example$ExampleAnnotation()\"" +
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
    public static @interface ExampleAnnotation {

    }
}
