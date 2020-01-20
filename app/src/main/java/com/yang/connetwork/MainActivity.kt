package com.yang.connetwork

import android.annotation.SuppressLint
import com.yang.connetwork.base.BaseAcivity
import com.yang.connetwork.network.APIFinishCallback
import com.yang.connetwork.network.NetWorkBasicResponse
import com.yang.connetwork.network.NetWorkRequestClient
import com.yang.connetwork.network.error.NetWorkHttpErrorHandler
import com.yang.library.loghandler.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAcivity() {
    override fun initListener() {
        bt.setOnClickListener {
            val api = GetDataListApi()
            NetWorkRequestClient.execute(api, object : APIFinishCallback {
                @SuppressLint("SetTextI18n")
                override fun OnRemoteApiFinish(response: NetWorkBasicResponse) {
                    if (response.mStatus == 0) {
                        val r = response as GetDataListApi.GetDataListResponse
                        tv.text = r.data
                        Log.printJson("NetWork",r.data,"")

                    } else {
                        tv.text = """msg${response.mMsg}status${response.mStatus}"""
                    }
                }
            })
        }
    }

    override fun initData() {

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
