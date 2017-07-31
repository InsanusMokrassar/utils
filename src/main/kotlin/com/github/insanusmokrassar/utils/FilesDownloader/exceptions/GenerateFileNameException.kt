package com.github.insanusmokrassar.utils.FilesDownloader.exceptions

import java.io.IOException

class GenerateFileNameException : IOException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
