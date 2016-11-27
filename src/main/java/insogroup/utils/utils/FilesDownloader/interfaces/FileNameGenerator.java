package insogroup.utils.utils.FilesDownloader.interfaces;

import insogroup.utils.utils.FilesDownloader.exceptions.GenerateFileNameException;

public interface FileNameGenerator {
    /**
     * Must generate the name for file
     * @param url Src url (can be used for generating name)
     * @return Name of file
     */
    String generateName(String url) throws GenerateFileNameException;
}
