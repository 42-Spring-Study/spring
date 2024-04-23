package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Slf4j
@Controller
public class ErrorPageController {
    /**
     * NOTE: 설명
     *  ERROR_EXCEPTION: 예외
     *  ERROR_EXCEPTION_TYPE: 예외 타입
     *  ERROR_MESSAGE: 오류 메시지
     *  ERROR_REQUEST_URI: 클라이언트 요청 URI
     *  ERROR_SERVLET_NAME: 오류 발생 서블릿 명
     *  ERROR_STATUS_CODE: HTTP 상태코드 명
     */
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    @GetMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @GetMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @GetMapping("/error-page/Ex")
    public String errorPageEx(HttpServletRequest request){
        log.info("errorPage Ex");
        printErrorInfo(request);
        return "error-page/500";
    }

    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex={}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: ex={}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: ex={}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: ex={}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: ex={}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: ex={}", request.getAttribute(ERROR_STATUS_CODE));
    }
}
