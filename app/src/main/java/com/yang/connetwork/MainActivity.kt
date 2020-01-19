package com.yang.connetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yang.connetwork.network.APIFinishCallback
import com.yang.connetwork.network.NetWorkBasicResponse
import com.yang.connetwork.network.NetWorkRequestClient
import com.yang.library.loghandler.Log.printJson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val api = GetDataListApi()
        NetWorkRequestClient.execute(api, object : APIFinishCallback {
            override fun OnRemoteApiFinish(response: NetWorkBasicResponse) {
                printJson("ConNetWork",response.toString(), "请求JSON")
            }
        })


    }
}
