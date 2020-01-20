package com.yang.connetwork.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yang.library.loghandler.ErrorHandler

/**
 * @author yangzc
 *	@data 2019/9/26 18:35
 *	@desc BaseAcivity
 *
 */
abstract class BaseAcivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
        initErrorHandler()
    }

    private fun initErrorHandler() {
        ErrorHandler.registerNewErrorHandler(this)
    }

    abstract fun initListener()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     *  加载布局
     */
    open fun layoutId(): Int {
        return 0
    }


}