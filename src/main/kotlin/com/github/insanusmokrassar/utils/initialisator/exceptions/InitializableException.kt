package com.github.insanusmokrassar.utils.initialisator.exceptions

import java.io.IOException

/**
 * Exception will called when object can't be initialised
 */
class InitializableException : IOException {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}
}
