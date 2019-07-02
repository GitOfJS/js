package hjs.config;

import hjs.controller.intercepter.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:D:/project_video/")
                .addResourceLocations("classpath:META-INF/resources");
    }
    @Bean
    public MyInterceptor myInterceptor(){
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor()).addPathPatterns("/user/**").addPathPatterns("/video/like")
                .addPathPatterns("/video/unLike").addPathPatterns("/video/saveComment").addPathPatterns("/bgm/**").excludePathPatterns("/user/queryUserInfo");
    }
}
