package com.github.insanusmokrassar.utils.FilesDownloader

import com.github.insanusmokrassar.utils.FilesDownloader.FileNameGenerators.PatternFileNameGenerator
import com.github.insanusmokrassar.utils.FilesDownloader.interfaces.FileNameGenerator

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class FilesDownloader {

    protected var fileNameGenerator: FileNameGenerator

    /**
     * List of lambdas. As first argument will url, as second - message (optional)
     */
    protected var downloadCompleteListeners: MutableList< (String, String) -> Unit> = ArrayList()
    /**
     * List of lambdas. As first argument will url, as second - message (optional)
     */
    protected var downloadFailureListeners: MutableList<(String, String?) -> Unit> = ArrayList()

    constructor() {
        fileNameGenerator = PatternFileNameGenerator(Regex("[-a-zA-Z0-9+&@#%?=~_|!:,.;]+(\\.[-a-zA-Z0-9+&@#%?=~_|!:,.;]+)*(\\/[-a-zA-Z0-9+&@#%?=~_|!:,.;]+(\\.[-a-zA-Z0-9+&@#%?=~_|!:,.;]+)*)*$"))
    }

    /**
     * @param pattern
     * * <pre>
     * *     The pattern is a string with regexp for finding filename in src url
     * * </pre>
     */
    constructor(pattern: Regex) {
        fileNameGenerator = PatternFileNameGenerator(pattern)
    }

    /**
     * @param fileNameGenerator Custom FileNameGenerator
     */
    constructor(fileNameGenerator: FileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator
    }

    /**
     * Download files if rootFolder
     * @param rootFolder Folder for placing files
     * *
     * @param urls Source urls which will used for download files and generating names
     * *
     * @return List of paths for downloaded files
     */
    fun downloadFiles(rootFolder: String, vararg urls: String): List<String> {

        val resultPaths = ArrayList<String>()

        for (url in urls) {
            try {
                val path = downloadFile(rootFolder, url)

                resultPaths.add(path)

                notifyComplete(url, path)
            } catch (e: IOException) {
                notifyFail(url, e.message)
            }

        }

        return resultPaths
    }

    @Throws(IOException::class)
    fun downloadFile(rootFolder: String, urlString: String): String {
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.requestMethod = "GET"
        urlConnection.connect()

        val file = File(rootFolder, fileNameGenerator.generateName(urlString))

        createFolders(file.absolutePath)

        if (file.exists()) {
            return file.absolutePath
        } else {
            file.createNewFile()
        }

        val fos = FileOutputStream(file)
        val inputStream = urlConnection.inputStream

        val buffer = ByteArray(1024)
        var bufferLength = inputStream.read(buffer)


        do {
            fos.write(buffer, 0, bufferLength)
            bufferLength = inputStream.read(buffer)
        } while (bufferLength > 0)

        fos.close()
        inputStream.close()

        notifyComplete(urlString, file.absolutePath)

        return file.absolutePath
    }

    @Throws(IllegalStateException::class)
    protected fun createFolders(absolutePath: String) {
        var rootPath = ""
        absolutePath.folders().forEach {
            val file = File(rootPath, it)
            if (file.exists()) {
                if (!file.isDirectory) {
                    throw IllegalStateException("Can't create file for ${file.absolutePath}: it is not directory.")
                }
            } else {
                file.mkdir()
            }
            rootPath = "$rootPath/$it"
        }
    }

    protected fun notifyComplete(url: String, path: String) {
        downloadCompleteListeners.forEach {
            it(url, path)
        }
    }

    protected fun notifyFail(url: String, message: String?) {
        downloadFailureListeners.forEach {
            it(url, message)
        }
    }

    fun subscribeSuccessful(listener:  (String, String) -> Unit) {
        downloadCompleteListeners.add(listener)
    }

    fun unSubscribe(listener:  (String, String) -> Unit) {
        downloadCompleteListeners.remove(listener)
    }

    fun subscribeFailure(listener: (String, String?) -> Unit) {
        downloadFailureListeners.add(listener)
    }

    fun unSubscribeFailure(listener: (String, String?) -> Unit) {
        downloadFailureListeners.remove(listener)
    }
}

private fun String.folders(): List<String> {
    val splitted = split("/")
    return splitted.minus(splitted.last())
}
