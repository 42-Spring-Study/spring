package hello.exception;

import hello.exception.filter.LogFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import lombok.extern.java.Log;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Bean
    public FilterRegistrationBean<Filter> logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setDispatcherTypes(DispatcherType.ERROR, DispatcherType.REQUEST);
        return filterRegistrationBean;
    }
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        // NOTE: RuntimeException 의 자식까지 모두 처리
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/Ex");
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
