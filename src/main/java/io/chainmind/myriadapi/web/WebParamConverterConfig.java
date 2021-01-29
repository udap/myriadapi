package io.chainmind.myriadapi.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import com.fasterxml.jackson.databind.Module;

@Configuration
public class WebParamConverterConfig {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";


    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                	return DateUtils.parseDate(source, new String[] {DEFAULT_DATE_TIME_FORMAT});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    @Bean
    public Converter<String, UUID> uuidConverter() {
        return new Converter<String, UUID>() {
            @Override
            public UUID convert(String source) {
                return UUID.fromString(source);
            }
        };
    }

    /**
     * feign 的page分页序列化支持
     * @return
     */
    @Bean
    public Module pageJacksonModule() { 
    	return new PageJacksonModule(); 
	}

    /**
     * feign 的自动转换器
     */
    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        return (FormatterRegistry formatterRegistry) -> {
            //LocalDate转换成String
            formatterRegistry.addConverter(LocalDate.class, String.class,localDateToStringConverter());
        };
    }

    @Bean
    public Converter<LocalDate,String> localDateToStringConverter() {
        return new Converter<LocalDate, String>() {
            @Override
            public String convert(LocalDate source) {
                if (source == null) {
                    return null;
                }
                return source.format(DateTimeFormatter.ofPattern(WebParamConverterConfig.DEFAULT_DATE_FORMAT));
            }
        };
    }

}
