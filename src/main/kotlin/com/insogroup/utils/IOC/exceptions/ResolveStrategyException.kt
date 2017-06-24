package com.insogroup.utils.IOC.exceptions

import java.io.IOException

class ResolveStrategyException : IOException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
