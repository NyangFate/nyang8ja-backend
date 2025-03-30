package com.nyang8ja.api.global.config

import com.nyang8ja.api.common.util.JwtUtil
import com.nyang8ja.api.external.notification.client.BizNotificationClient
import com.nyang8ja.api.global.filter.ExceptionHandleFilter
import com.nyang8ja.api.global.filter.TraceFilter
import com.nyang8ja.api.global.resolver.RequestUserArgumentResolver
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterRegistration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val jwtUtil: JwtUtil
): WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(com.nyang8ja.api.global.resolver.RequestUserArgumentResolver(jwtUtil))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600)
    }

    @Bean
    fun addTraceFilter(): FilterRegistrationBean<Filter> {
        val filterRegistration = FilterRegistrationBean<Filter>()
        filterRegistration.filter = TraceFilter()
        filterRegistration.order = Integer.MIN_VALUE
        filterRegistration.addUrlPatterns("/*")
        return filterRegistration
    }

    @Bean
    fun addExceptionHandleFilter(objectMapper: ObjectMapper, bizNotificationClient: com.nyang8ja.api.external.notification.client.BizNotificationClient): FilterRegistrationBean<Filter> {
        val filterRegistration = FilterRegistrationBean<Filter>()
        filterRegistration.filter = ExceptionHandleFilter(objectMapper, bizNotificationClient)
        filterRegistration.order = Integer.MIN_VALUE + 1
        filterRegistration.addUrlPatterns("/*")
        return filterRegistration
    }
}