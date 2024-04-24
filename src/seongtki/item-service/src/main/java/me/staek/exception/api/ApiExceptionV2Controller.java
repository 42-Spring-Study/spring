package me.staek.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.staek.exception.exception.UserException;
import me.staek.exception.exhandler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ExceptionHandlerExceptionResolver 사용을 위한 컨트롤러
 * - 스프링에서 제공하는기능이고, ExceptionResolver 중 우선순위가 가장 높다.
 * <p>
 * @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다.
 * 해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다. (예외와 그 자식까지 캐치 가능)
 */
@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다.
     * 예외가 발생했으로 ExceptionResolver 가 작동한다. 가장 우선순위가 높은
     * ExceptionHandlerExceptionResolver 가 실행된다.
     * ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리
     * 할 수 있는 @ExceptionHandler 가 있는지 확인한다.
     * illegalExHandle() 를 실행한다. @RestController 이므로 illegalExHandle() 에도
     * @ResponseBody 가 적용된다. 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환된다.
     * @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로 응답한다
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    /**
     * @ExceptionHandler 에 예외를 지정하지 않으면 해당 메서드 파라미터 예외를 사용한다.(UserException)
     * ResponseEntity 를 사용해서 HTTP 메시지 바디에 직접 응답한다.(HTTP 컨버터)
     * ResponseEntity 를 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경할 수 있다.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * throw new RuntimeException("잘못된 사용자") 이 코드가 실행되면서, 컨트롤러 밖으로 RuntimeException throw
     * RuntimeException 은 Exception 의 자식 클래스이다. 따라서 이 메서드가 호출된다.
     * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 로 HTTP 상태 코드를 500으로 응답한다.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
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
}
