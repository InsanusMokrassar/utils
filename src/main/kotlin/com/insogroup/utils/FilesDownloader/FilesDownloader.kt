package com.insogroup.utils.FilesDownloader

import com.insogroup.utils.FilesDownloader.FileNameGenerators.PatternFileNameGenerator
import com.insogroup.utils.FilesDownloader.exceptions.GenerateFileNameException
import com.insogroup.utils.FilesDownloader.interfaces.FileNameGenerator

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

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

    /**
     * @param pattern
     * * <pre>
     * *     The pattern is a string with regexp for finding filename in src url
     * * </pre>
     */
    constructor(pattern: String) {
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

        val folder = File(rootFolder)

        val file = File(rootFolder, getFileName(urlString))

        if (!folder.exists()) {
            folder.mkdirs()
        }

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

        return file.absolutePath
    }

    @Throws(GenerateFileNameException::class)
    protected fun getFileName(url: String): String {
        return fileNameGenerator.generateName(url)
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
