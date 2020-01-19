package com.yang.connetwork;

import com.yang.connetwork.network.NetWorkServerApi;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author yangzc
 * @data 2020/1/16 17:02
 * @desc
 */
public class NetWorkApi extends NetWorkServerApi {


    public NetWorkApi(@NotNull String mRelativeURL) {
        super(mRelativeURL);
    }

    @NotNull
    @Override

    /**
     * 调整环境
     */
    protected String getServerUrl() {
        return BuildConfig.DEBUG ? Constants.RELEASE_URL : Constants.TEST_URL;
    }


    /**
     * 请求头
     */
    @NotNull
    @Override
    public Map<String, String> getSystemHeader() {
        return super.getSystemHeader();
    }
}
