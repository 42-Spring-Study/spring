package me.staek.itemservice.web.filter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import me.staek.exception.resolver.MyHandlerExceptionResolver;
import me.staek.exception.resolver.UserHandlerExceptionResolver;
import me.staek.itemservice.argumentresolver.LoginMemberArgumentResolver;
import me.staek.itemservice.interceptor.LogInterceptor;
import me.staek.itemservice.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebFilterConfig implements WebMvcConfigurer {

    /**
     * setFilter(new LogFilter()) : 등록할 필터를 지정한다.
     * setOrder(1) : 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작한다.
     * addUrlPatterns("/*") : 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다
     * <p>
     * @ServletComponentScan @WebFilter(filterName = "logFilter", urlPatterns = "/*") 로 필터 등록이 가능하지만 필터 순서 조절이 안된다.
     */
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogFilter());
        bean.setOrder(1);
        /**
         * 두 가지를 모두 넣으면 클라이언트 요청,오류 페이지 요청에서도 필터 모두 호출됨.
         * 아무것도 넣지 않으면 기본 값이 DispatcherType.REQUEST 이다.( 클라이언트의 요청이 있는 경우에만 필터적용)
         * (오류 페이지 요청 전용 필터를 적용하고 싶으면 DispatcherType.ERROR 만 지정하면 된다)
         */
        bean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
//        bean.setDispatcherTypes(DispatcherType.REQUEST);
        bean.addUrlPatterns("/*");
        return bean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> bean = new
                FilterRegistrationBean<>();
        bean.setFilter(new LoginCheckFilter());
        bean.setOrder(2);
        bean.addUrlPatterns("/*");
        return bean;
    }

    /**
     * 필터와 비교해보면 인터셉터는 addPathPatterns , excludePathPatterns 로 매우 정밀하게 URL 패턴을 지정할 수 있다
     * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         *
         * 오류발생 인터셉터는 경로 정보로 중복 호출 제거( excludePathPatterns("/error-page/**") 가능하다.
         */
        // 로그
//        registry.addInterceptor(new LogInterceptor())
//                .order(1)
//                .addPathPatterns("/**")
////                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**");
//                .excludePathPatterns("/css/**", "/*.ico", "/error");


        // 로그인 체크
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/", "/members/add", "/login", "/logout",
//                        "/css/**", "/*.ico", "/error"
//                );
    }

    /**
     * 로그인 체크 argumentResolver
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }


    /**
     * HandlerExceptionResolver 추가
     * - 기본 설정을 유지하면서 추가됨
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }
}
