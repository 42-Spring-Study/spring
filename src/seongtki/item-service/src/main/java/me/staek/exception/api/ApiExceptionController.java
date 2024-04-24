package me.staek.exception.api;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.staek.exception.exception.BadRequestException;
import me.staek.exception.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiExceptionController {
    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        /**
         * @IllegalArgumentException
         * http://localhost:8080/api/members/bad 호출 시 500코드가 반환됨을 알 수 있다.
         * 예외코드 변경을 위해 HandlerExceptionResolver를 이용할 수 있다.
         */
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        /**
         * @UserException
         */
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }


        return new MemberDto(id, "hello " + id);
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }


    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";


    /**
     * WebServerCustomizer에서 매핑한 에러코드에 대한 메서드가 아래결로 호출된다. (ServletExController 에 같은 url매핑이 있지만 produces로 구분된다)
     * <p>
     * produces = MediaType.APPLICATION_JSON_VALUE
     * => 클라이언트가 요청하는 HTTP Header의 Accept 의 값이 application/json 일 때 해당 메서드가 호출됨
     * => 클라어인트가 받고 싶은 미디어 타입이 json이면 해당 컨트롤러의 메서드가 호출된다.
     * => ResponseEntity 를 사용해서 응답하기 때문에 메시지 컨버터가 동작하면서 클라이언트에 JSON이 반환된다
     */
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest
                                                                       request, HttpServletResponse response) {
        log.info("API errorPage 500");
        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());
        Integer statusCode = (Integer)
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity(result, HttpStatus.valueOf(statusCode));
    }

    /**
     * ResponseStatusExceptionResolver 가 발생한 예외의
     * @ResponseStatus 를 확인해서 오류코드를 조정한다.
     * - (response.sendError(statusCode, resolvedReason) 호출하는 식으로 조정함)
     */
    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }
}
