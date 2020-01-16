package com.yang.connetwork.network

interface NetWorkHttpError {

    fun getErrorMessage(errorCode: Int): String

    fun interceptError(resp: NetWorkBasicResponse): NetWorkBasicResponse

    companion object {

        val NOT_AVAILABLE_NETWORK_ = -1

        val NETWORK_EXCEPTION = 2

        val JSON_EXCEPTION = 3

        val NOT_DATA_EXCEPTION = 4
    }
}
