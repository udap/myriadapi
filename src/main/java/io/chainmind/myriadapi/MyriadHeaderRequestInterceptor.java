package io.chainmind.myriadapi;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.chainmind.myriadapi.domain.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyriadHeaderRequestInterceptor implements RequestInterceptor {
    private final RequestUser requestUser;

    @Autowired
    public MyriadHeaderRequestInterceptor(RequestUser requestUser) {
        this.requestUser = requestUser;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("x-request-user", requestUser.getReqUser());
    }

}