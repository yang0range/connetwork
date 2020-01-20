package com.yang.connetwork.network

interface NetWorkHttpError {

    fun getErrorMessage(errorCode: Int): String

    fun interceptError(resp: NetWorkBasicResponse): NetWorkBasicResponse

    companion object {

        var NOT_AVAILABLE_NETWORK = -1

        var NETWORK_EXCEPTION = 2

        var JSON_EXCEPTION = 3

        var NOT_DATA_EXCEPTION = 4
    }
}
