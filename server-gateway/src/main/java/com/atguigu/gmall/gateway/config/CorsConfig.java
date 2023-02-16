package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

/**
 * @author Pch
 * @create 2023-02-10 18:54
 * @description:相当于一个配置类
 */

@Configuration
public class CorsConfig {

    /**
     * 设置跨域：
     *
     * @return WebFilter
     */
    @Bean
    public WebFilter webFilter() {
        //CorsConfiguration
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //设置域名
        corsConfiguration.addAllowedOrigin("*");
        //设置请求头
        corsConfiguration.addAllowedHeader("*");
        //设置方法
        corsConfiguration.addAllowedMethod("*");
        //携带cookie
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        //返回数据
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
