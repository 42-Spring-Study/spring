package me.staek.exception.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import me.staek.exception.exception.UserException;
import me.staek.exception.exhandler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**

 * @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능을 부여해주는 역할을 한다.
 * @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
 * @RestControllerAdvice 는 @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.
 * @Controller , @RestController 의 차이와 같다.
 *
 * <p>
 * ** 대상컨트롤러 지정 방법 **
 * https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-advice.html
 * => 특정 애노테이션이 있는 컨트롤러를 지정할 수 있고, 특정 패키지를 직접 지정할 수도 있다.
 * => 패키지 지정의 경우 해당 패키지와 그 하위에 있는 컨트롤러가 대상이 된다.
 * => 특정 클래스를 지정할 수도 있다.
 * => 대상 컨트롤러 지정을 생략하면 모든 컨트롤러에 적용된다.
 * <p>
 * // Target all Controllers annotated with @RestController
 * @ControllerAdvice(annotations = RestController.class)
 * public class ExampleAdvice1 {}
 * // Target all Controllers within specific packages
 * @ControllerAdvice("org.example.controllers")
 * public class ExampleAdvice2 {}
 * // Target all Controllers assignable to specific classes
 * @ControllerAdvice(assignableTypes = {ControllerInterface.class,AbstractController.class})
 * public class ExampleAdvice3 {}
 */
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
