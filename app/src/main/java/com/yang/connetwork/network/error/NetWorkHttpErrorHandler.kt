package com.yang.connetwork.network.error

import android.content.Context

import com.yang.connetwork.R
import com.yang.connetwork.network.NetWorkBasicResponse
import com.yang.connetwork.network.NetWorkHttpError
import com.yang.connetwork.network.utils.NetworkUtils


/**
 * Created by wzh on 2016/5/19.
 * Desc:
 */
class NetWorkHttpErrorHandler(private val mContext: Context) : NetWorkHttpError {

    override fun getErrorMessage(errorCode: Int): String {
        when (errorCode) {
            NetWorkHttpError.JSON_EXCEPTION -> return mContext.getString(R.string.network_timeout)
            NetWorkHttpError.JSON_EXCEPTION -> return mContext.getString(R.string.json_exception)
            NetWorkHttpError.JSON_EXCEPTION -> return mContext.getString(R.string.network_result_null)
            NetWorkHttpError.NOT_AVAILABLE_NETWORK -> return if (NetworkUtils.isNetworkAvaliable(
                    mContext
                )
            ) {
                mContext.getString(R.string.network_timeout)
            } else {
                mContext.getString(R.string.network_unavailable)
            }
            404 -> return mContext.getString(R.string.http_error_code_404)
            503 -> return mContext.getString(R.string.http_error_code_503)
            500 -> return mContext.getString(R.string.http_error_code_500)
            408 -> return mContext.getString(R.string.network_timeout)
            502 -> return mContext.getString(R.string.network_timeout)
            else -> return mContext.getString(R.string.network_timeout) + "[" + errorCode + "]"
        }
    }


    override fun interceptError(resp: NetWorkBasicResponse): NetWorkBasicResponse {
        return resp
    }
}