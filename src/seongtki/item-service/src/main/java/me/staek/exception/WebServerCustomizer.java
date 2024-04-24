package me.staek.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ** 등록 **
 * 서블릿은 Exception이 발생해서 서블릿 밖으로 전파 되거나 또는 response.sendError() 가 호출 되었을 때
 * 각각의 상황에 맞춘 오류 처리 기능을 제공할 수 있다.
 * <p>
 * 서블릿은 web.xml 파일에 아래처럼 에러페이지를 등록할 수 있다.
 * <web-app>
 *  <error-page>
 *  <error-code>404</error-code>
 *  <location>/error-page/404.html</location>
 *  </error-page>
 *  <error-page>
 *  <error-code>500</error-code>
 *  <location>/error-page/500.html</location>
 *  </error-page>
 *  <error-page>
 *  <exception-type>java.lang.RuntimeException</exception-type>
 *  <location>/error-page/500.html</location>
 *  </error-page>
 * </web-app>
 * <p>
 * 지금은 스프링 부트를 통해서 서블릿 컨테이너를 실행하기 때문에, 스프링 부트가 제공하는 기능을 사용해서 서블릿 오류 페이지를
 * 등록할 수 있다.
 *
 * <p>
 * ** 해제 **
 * 스프링 부트 오류발생에 대한 페이지 등을 자동으로 등록해준다. (이때 /error 라는 경로로 기본 오류 페이지를 설정한다. - new ErrorPage("/error"))
 * 상태코드와 예외를 설정하지 않으면 이를 기본 오류 페이지로 사용된다.
 * 서블릿 밖으로 예외가 발생하거나, response.sendError(...) 가 호출되면 모든 오류는 /error를 호출하게 된다.
 * BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록한다. (ErrorPage 에서 등록한 /error 를 매핑해서 처리하는 컨트롤러다.)
 * *** 스프링 부트가 제공하는 기본 오류 메커니즘을 사용하도록 WebServerCustomizer에 있는 @Component 를 주석처리해야한다.
 */
//@Component
public class WebServerCustomizer implements
        WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
