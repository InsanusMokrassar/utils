package com.github.insanusmokrassar.utils

import com.github.insanusmokrassar.utils.FilesDownloader.FilesDownloader
import com.github.insanusmokrassar.utils.IOC.IOC
import com.github.insanusmokrassar.utils.IOC.interfaces.IOCStrategy
import java.io.FileInputStream

import java.io.IOException
import java.util.logging.LogManager


/**
 * Need "LOGGER_CONFIG_PATH" environment variable which set the place where was put .properties file with config of logger
 */
fun loadLoggerConfig() {
    FileInputStream(System.getenv("LOGGER_CONFIG_PATH")).use {
        LogManager.getLogManager().readConfiguration(it)
    }
}

/**
 * Need file with logger config
 */
fun loadLoggerConfig(file: FileInputStream) {
    LogManager.getLogManager().readConfiguration(file)
}

private val exampleOfCustomStrategyInitializerConfig = "{\n" +
        "   \"com.insogroup.utils.IOC.initializators.CustomIOCStrategyInitializator\":{\n" +
        "       \"com.github.insanusmokrassar.SimpleIOCStrategy\" : {\n" +
        "           \"ArrayQueue\" : \"com.insogroup.utils.queues.ArrayQueue\"\n" +
        "       },\n" +
        "       \"com.github.insanusmokrassar.CacheIOCStrategy\" : {\n" +
        "           \"ArrayQueueCached\" : \"com.insogroup.utils.queues.ArrayQueue\"\n" +
        "       },\n" +
        "       \"com.github.insanusmokrassar.ProtectedCallingIOCStrategy\" : {\n" +
        "           \"ArrayQueueCachedProtected\" : {" +
        "               \"targetStrategy\" : \"com.github.insanusmokrassar.CacheIOCStrategy\"," +
        "               \"targetStrategyParams\" : \"com.insogroup.utils.queues.ArrayQueue\"," +
        "               \"requiredAnnotations\" : [" +
        "                   \"@com.insogroup.utils.Example\$ExampleAnnotation()\"" +
        "               ]" +
        "           }\n" +
        "       }\n" +
        "   }\n" +
        "}\n"

/**
 * Add environment variable "OUTPUT_DIR" to get out files in specific directory.
 * Add environment variable "DOWNLOAD_FILE" with link to some remote file to download it.
 */
@Throws(IOException::class)
fun main(args: Array<String>) {

    val outputDirectory: String
    if (System.getenv("OUTPUT_DIR") == null) {
        outputDirectory = System.getenv("PWD")
    } else {
        outputDirectory = System.getenv("OUTPUT_DIR")
    }

    val IOC = IOC()

    val outputStreamCallback = {
        whatNew: String ->
        println("OutputStream out: $whatNew")
    }

    IOC.subscribe(outputStreamCallback)

    IOC.register("Example", object: IOCStrategy {
        override fun <T> getInstance(vararg args: Any): T {
            println("Try to get instance")
            return outputStreamCallback as T
        }
    })

    IOC.resolve<(String) -> Unit>("Example")("The instance which was get output it")

    val downloadFile = System.getenv("DOWNLOAD_FILE")
    if (downloadFile != null) {
        val fileDownloader = FilesDownloader()
        fileDownloader.subscribeSuccessful({
            url:String,
            path: String ->
            outputStreamCallback("File \"$url\" successfully downloaded in \"$path\"")
        })
        fileDownloader.subscribeFailure({
            url: String,
            whyNot: String? ->
            outputStreamCallback("Can't download file \"$url\" fore next reason:\"$whyNot\"")
        })
        fileDownloader.downloadFile(outputDirectory, downloadFile)
    }
}
