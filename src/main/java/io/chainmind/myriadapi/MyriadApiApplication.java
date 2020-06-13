package io.chainmind.myriadapi;

import io.chainmind.myriad.domain.RequestUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class MyriadApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(MyriadApiApplication.class, args);
    }

    @Bean
    @RequestScope
    public RequestUser getCurrentUser() {
        return new RequestUser();
    }

}
