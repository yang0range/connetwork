package com.yang.connetwork;

import com.yang.connetwork.network.NetWorkApi;
import com.yang.connetwork.network.NetWorkBasicResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yangzc
 * @data 2020/1/19 14:22
 * @desc
 */
public class GetDataListApi extends NetWorkApi {
    private static final String mURL = "2/json";


    public GetDataListApi() {
        super(mURL);
    }

    @Override
    public int getHttpRequestType() {
        return NetWorkApi.Companion.getHTTP_REQUEST_TYPE_GET();
    }


    @Override
    public NetWorkBasicResponse parseResponseBase(JSONObject json) {
        try {
            return new GetDataListResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class GetDataListResponse extends NetWorkBasicResponse {

        public GetDataListResponse(@NotNull JSONObject json) throws JSONException {
            super(json);
        }
    }


}
