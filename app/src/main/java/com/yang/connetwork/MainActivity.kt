package com.yang.connetwork

import com.yang.connetwork.base.BaseAcivity
import com.yang.connetwork.network.APIFinishCallback
import com.yang.connetwork.network.NetWorkBasicResponse
import com.yang.connetwork.network.NetWorkRequestClient
import com.yang.connetwork.network.error.NetWorkHttpErrorHandler
import com.yang.library.loghandler.Log

class MainActivity : BaseAcivity() {
    override fun initListener() {
    }

    override fun initData() {
        val api = GetDataListApi()
        NetWorkRequestClient.execute(api, object : APIFinishCallback {
            override fun OnRemoteApiFinish(response: NetWorkBasicResponse) {
                Log.printJson("ConNetWork", response.toString(), "请求JSON")
            }
        })
    }

    override fun initView() {
        NetWorkRequestClient.init(
            applicationContext,
            NetWorkHttpErrorHandler(applicationContext),
            BuildConfig.DEBUG
        )
    }

    override fun start() {
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

}
