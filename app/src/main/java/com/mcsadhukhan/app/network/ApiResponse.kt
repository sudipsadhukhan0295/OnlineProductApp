package com.mcsadhukhan.app.network

import java.lang.Exception


class ApiResponse<T> {
    var responseBody: T? = null
    var exception: Exception? = null

    constructor(responseBody: T?) {
        this.responseBody = responseBody
    }

    constructor(exception: Exception?) {
        this.exception = exception
    }
}