package com.insogroup.utils.FilesDownloader;

import com.insogroup.utils.FilesDownloader.FileNameGenerators.PatternFileNameGenerator;
import com.insogroup.utils.FilesDownloader.exceptions.GenerateFileNameException;
import com.insogroup.utils.FilesDownloader.interfaces.DownloadEventListener;
import com.insogroup.utils.FilesDownloader.interfaces.FileNameGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FilesDownloader {

    protected FileNameGenerator fileNameGenerator;

    protected List<DownloadEventListener> listeners = new ArrayList<>();

    /**
     * @param pattern
     * <pre>
     *     The pattern is a string with regexp for finding filename in src url
     * </pre>
     */
    public FilesDownloader(String pattern) {
        fileNameGenerator = new PatternFileNameGenerator(pattern);
    }

    /**
     * @param fileNameGenerator Custom FileNameGenerator
     */
    public FilesDownloader(FileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }

    /**
     * Download files if rootFolder
     * @param rootFolder Folder for placing files
     * @param urls Source urls which will used for download files and generating names
     * @return List of paths for downloaded files
     */
    public List<String> downloadFiles(String rootFolder, String... urls) {

        List<String> resultPaths = new ArrayList<>();

        for (String url : urls) {
            try {
                String path = downloadFile(rootFolder, url);

                resultPaths.add(path);

                notifyComplete(url, path);
            } catch (IOException e) {
                notifyFail(url, e.getMessage());
            }
        }

        return resultPaths;
    }

    public String downloadFile(String rootFolder, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        File folder = new File(rootFolder);

        File file = new File(rootFolder, getFileName(urlString));

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        InputStream inputStream = urlConnection.getInputStream();

        byte[] buffer = new byte[1024];
        Integer bufferLength = 0;

        while ((bufferLength = inputStream.read(buffer)) > 0) {
            fos.write(buffer, 0, bufferLength);
        }

        fos.close();
        inputStream.close();

        return file.getAbsolutePath();
    }

    protected String getFileName(String url) throws GenerateFileNameException {
        return fileNameGenerator.generateName(url);
    }

    protected void notifyComplete(String url, String path) {
        for (DownloadEventListener listener : listeners) {
            listener.downloadComplete(url, path);
        }
    }

    protected void notifyFail(String url, String message) {
        for (DownloadEventListener listener : listeners) {
            listener.downloadFail(url, message);
        }
    }

    public void subscribe(DownloadEventListener listener) {
        listeners.add(listener);
    }

    public void unSubscribe(DownloadEventListener listener) {
        listeners.remove(listener);
    }
}
