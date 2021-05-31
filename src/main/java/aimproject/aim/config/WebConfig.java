package aimproject.aim.config;

import aimproject.aim.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/css/**", "/resources/**", "/static/images/**", "/javascript/**")
                .excludePathPatterns("/imageAnalysis", "/result","/result/**", "/login", "/join", "/logout")
                .excludePathPatterns("/member/form", "/login", "/login/form", "/image/**" , "/imageUpload/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(20);

        registry.addResourceHandler("/result/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(20);

    }
}