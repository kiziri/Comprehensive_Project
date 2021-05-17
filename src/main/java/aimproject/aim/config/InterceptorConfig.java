package aimproject.aim.config;

import aimproject.aim.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/css/**", "/resources/**","/images/**", "/javascript/**")
                .excludePathPatterns("/imageAnalysis", "/result", "/login", "/join", "/logout")
                .excludePathPatterns("/member/form", "/login", "/login/form");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
                .addResourceLocations("/css/**")
                .addResourceLocations("/css/common/**")
                .addResourceLocations("/javascript/**")
                .setCachePeriod(20);
    }
}