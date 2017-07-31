package com.github.insanusmokrassar.utils.FilesDownloader.FileNameGenerators

import com.github.insanusmokrassar.utils.FilesDownloader.exceptions.GenerateFileNameException
import com.github.insanusmokrassar.utils.FilesDownloader.interfaces.FileNameGenerator

class PatternFileNameGenerator(val regex: Regex) : FileNameGenerator {

    @Throws(GenerateFileNameException::class)
    override fun generateName(url: String): String {
        val m = regex.toPattern().matcher(url)
        m.find()
        try {
            return m.group()
        } catch (e: IllegalStateException) {
            throw GenerateFileNameException(
                    "Can't generate filename for: \"$url\" with regexp \"${regex.pattern}\"")
        }

    }
}
