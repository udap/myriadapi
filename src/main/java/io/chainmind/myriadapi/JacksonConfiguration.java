package io.chainmind.myriadapi;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.chainmind.myriadapi.web.WebParamConverterConfig;

@Configuration
public class JacksonConfiguration {

    /**
     * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
     * 注意：这里配置的转换器，对RequestParam和PathVariable参数没用。
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJson() {
    	return builder -> {
            // exclude null values
            builder.serializationInclusion(JsonInclude.Include.NON_NULL)
            	.failOnUnknownProperties(false)
//                    .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .simpleDateFormat(WebParamConverterConfig.DEFAULT_DATE_TIME_FORMAT)
                    //.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)))
                    .serializers(
                            new LocalDateSerializer(DateTimeFormatter.ofPattern(WebParamConverterConfig.DEFAULT_DATE_FORMAT)),
                            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(WebParamConverterConfig.DEFAULT_DATE_TIME_FORMAT))
                    )
                    .deserializers(
                            new LocalDateDeserializer(DateTimeFormatter.ofPattern(WebParamConverterConfig.DEFAULT_DATE_FORMAT)),
                            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(WebParamConverterConfig.DEFAULT_DATE_TIME_FORMAT))
                    );
        };
    }
	/**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module().configureAbsentsAsNulls(true);
    }

    /*
     * Support for Hibernate types in Jackson.
     */
//    @Bean
//    public Hibernate5Module hibernate5Module() {
//        return new Hibernate5Module();
//    }

    /*
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
//    @Bean
//    public AfterburnerModule afterburnerModule() {
//        return new AfterburnerModule();
//    }

//    /*
//     * Module for serialization/deserialization of RFC7807 Problem.
//     */
//    @Bean
//    ProblemModule problemModule() {
//        return new ProblemModule();
//    }
//
//    /*
//     * Module for serialization/deserialization of ConstraintViolationProblem.
//     */
//    @Bean
//    ConstraintViolationProblemModule constraintViolationProblemModule() {
//        return new ConstraintViolationProblemModule();
//    }
}
