package com.insogroup.utils.ClassExtractor.exceptions

import java.io.IOException

/**
 * Throws when extraction class was failed
 */
class ClassExtractException : IOException {
    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
