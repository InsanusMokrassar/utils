package com.insogroup.utils

import com.github.insanusmokrassar.iobjectk.realisations.SimpleIObject
import com.insogroup.utils.IOC.IOC
import com.insogroup.utils.IOC.interfaces.IOCStrategy
import com.insogroup.utils.initialisator.realisations.StandardInitialisator
import com.insogroup.utils.initialisator.realisations.staticInit

import java.io.IOException
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


private val exampleOfCustomStrategyInitializerConfig = "{\n" +
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
        "                   \"@com.insogroup.utils.Example\$ExampleAnnotation()\"" +
        "               ]" +
        "           }\n" +
        "       }\n" +
        "   }\n" +
        "}\n"

@Throws(IOException::class)
fun main(args: Array<String>) {

    val IOC = IOC()

    val outputStreamCallback = {
        whatNew: String ->
        println("OutputStream out: $whatNew")
    }

    IOC.subscribe(outputStreamCallback)

    IOC.register("Example", object: IOCStrategy{
        override fun getInstance(vararg args: Any): Any {
            println("Try to get instance")
            return outputStreamCallback
        }
    })

    IOC.resolve<(String) -> Unit>("Example")("The instance which was get output it")
}
