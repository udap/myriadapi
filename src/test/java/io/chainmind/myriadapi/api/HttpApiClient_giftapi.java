//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package io.chainmind.myriadapi.api;
import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;

/**
 * http请求
 */
public class HttpApiClient_giftapi extends ApacheHttpClient{
    public final static String HOST = "giftapi-test.xinongtech.com";
    static HttpApiClient_giftapi instance = new HttpApiClient_giftapi();
    public static HttpApiClient_giftapi getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }





}