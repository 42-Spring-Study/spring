package me.staek.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
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
 */
@Component
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
