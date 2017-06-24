package com.insogroup.utils.FilesDownloader.FileNameGenerators

import com.insogroup.utils.FilesDownloader.exceptions.GenerateFileNameException
import com.insogroup.utils.FilesDownloader.interfaces.FileNameGenerator

import java.util.regex.Matcher
import java.util.regex.Pattern

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
