package com.github.insanusmokrassar.utils.FilesDownloader.interfaces

import com.github.insanusmokrassar.utils.FilesDownloader.exceptions.GenerateFileNameException

interface FileNameGenerator {
    /**
     * Must generate the name for file
     * @param url Src url (can be used for generating name)
     * @return Name of file
     */
    @Throws(GenerateFileNameException::class)
    fun generateName(url: String): String
}
