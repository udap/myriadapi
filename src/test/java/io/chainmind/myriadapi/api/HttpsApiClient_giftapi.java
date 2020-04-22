//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package io.chainmind.myriadapi.api;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
/**
 * https请求
 */
public class HttpsApiClient_giftapi extends ApacheHttpClient{
    public final static String HOST = "giftapi-test.xinongtech.com";
    static HttpsApiClient_giftapi instance = new HttpsApiClient_giftapi();
    public static HttpsApiClient_giftapi getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTPS);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }



    public void GetEmployee(String code , ApiCallback callback) {
        String path = "/employees/searchUnique";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("code" , code , ParamPosition.QUERY , true);
        //请求 API 的 Stage，目前支持 TEST、PRE、RELEASE 三个 Stage
//        request.addHeader("X-Ca-Stage","TEST");
        sendAsyncRequest(request , callback);
    }

    public ApiResponse GetEmployee_syncMode(String code) {
        String path = "/employees/searchUnique";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("code" , code , ParamPosition.QUERY , true);
        //请求 API 的 Stage，目前支持 TEST、PRE、RELEASE 三个 Stage
//        request.addHeader("X-Ca-Stage","TEST");
        return sendSyncRequest(request);
    }
    public ApiResponse GetCustomers_syncMode(String employeeUid) {
        String path = "/customers";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("employeeUid" , employeeUid , ParamPosition.QUERY , true);
        //请求 API 的 Stage，目前支持 TEST、PRE、RELEASE 三个 Stage
        request.addHeader("X-Ca-Stage","TEST");
        return sendSyncRequest(request);
    }



}