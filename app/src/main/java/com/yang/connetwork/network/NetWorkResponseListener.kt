package com.yang.connetwork.network

import android.text.TextUtils
import com.kymjs.rxvolley.client.HttpCallback
import com.yang.library.loghandler.Log
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers

class NetWorkResponseListener(
    private var mApi: NetWorkServerApi?,
    private val mListener: APIFinishCallback?
) : HttpCallback() {


    override fun onFailure(errorNo: Int, strMsg: String?) {
        super.onFailure(errorNo, strMsg)
        if (mApi == null) {
            return
        }
        val resp: NetWorkBasicResponse
        if (errorNo == 0) {
            resp = NetWorkBasicResponse(
                NetWorkHttpError.NETWORK_EXCEPTION,
                mApi!!.getHttpErrorMsg(NetWorkHttpError.NETWORK_EXCEPTION)
            )
        } else {
            resp = NetWorkBasicResponse(errorNo, mApi!!.getHttpErrorMsg(errorNo))
        }
        if (NetWorkRequestClient.mEnableLogging) {
            Log.e(
                NetWorkRequestClient.TAG,
                "tag=" + mApi!!.tag + "/Error status = " + resp.mStatus + " msg = " + strMsg
            )
        }
        mListener?.OnRemoteApiFinish(resp)
        mApi = null
    }

    override fun onSuccess(t: String?) {
        super.onSuccess(t)
        if (mApi == null) {
            return
        }
        if (TextUtils.isEmpty(t)) {
            val resp = NetWorkBasicResponse(
                NetWorkHttpError.NOT_DATA_EXCEPTION,
                mApi!!.getHttpErrorMsg(NetWorkHttpError.NOT_DATA_EXCEPTION)
            )
            mListener?.OnRemoteApiFinish(resp)
            if (NetWorkRequestClient.mEnableLogging) {
                Log.e(
                    NetWorkRequestClient.TAG,
                    "tag=" + mApi!!.tag + "/Fail status = " + resp.mStatus + " msg = " + resp.mMsg
                )
            }
            mApi = null
            return
        }

        Observable.just<String>(t).subscribeOn(Schedulers.newThread()).map { s ->
            var s = s
            var obj: JSONObject? = null
            try {
                obj = JSONObject(s!!)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            s = null
            obj
        }.map(Func1<JSONObject?, NetWorkBasicResponse> { jsonObject ->
            var jsonObject: JSONObject? = jsonObject
                ?: return@Func1 NetWorkBasicResponse(
                    NetWorkHttpError.JSON_EXCEPTION,
                    mApi!!.getHttpErrorMsg(NetWorkHttpError.JSON_EXCEPTION)
                )
            val resp = jsonObject?.let { mApi!!.parseResponseBase(it) }
            jsonObject = null
            resp
        }).observeOn(AndroidSchedulers.mainThread()).subscribe({ resp ->
            var resp = resp
            if (resp == null) {
                resp = NetWorkBasicResponse(
                    NetWorkHttpError.JSON_EXCEPTION,
                    mApi!!.getHttpErrorMsg(NetWorkHttpError.JSON_EXCEPTION)
                )
            }
            if (resp.mStatus != NetWorkBasicResponse.SUCCESS) {
                resp = mApi!!.interceptError(resp)
            }
            if (NetWorkRequestClient.mEnableLogging) {
                Log.e(
                    NetWorkRequestClient.TAG,
                    "tag=" + mApi!!.tag + "/ status = " + resp!!.mStatus + " msg = " + resp.mMsg
                )
            }
            mListener?.OnRemoteApiFinish(resp!!)
        }, { throwable ->
            if (mListener != null) {
                Log.e(NetWorkRequestClient.TAG, "[ERROR]" + throwable.message)
                mListener.OnRemoteApiFinish(
                    NetWorkBasicResponse(
                        999,
                        "数据处理异常"
                    )
                )
            }
        })
    }

}
