package me.staek.upload;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;
@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {
    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    /**
     * spring.servlet.multipart.enabled 옵션으로 멀티파트 업무 구분
     * true: StandardMultipartHttpServletRequest
     * => DispatcherServlet 에서 멀티파트 리졸버( MultipartResolver )를 실행한다
     * => 멀티파트 리졸버는 멀티파트 요청인 경우 서블릿 컨테이너가 전달하는 일반적인 HttpServletRequest 를MultipartHttpServletRequest 로 변환해서 반환한다.
     * => MultipartHttpServletRequest 는 HttpServletRequest 의 자식 인터페이스이고, 멀티파트와 관련된 추가 기능을 제공한다
     * => 스프링이 제공하는 기본 멀티파트 리졸버는 MultipartHttpServletRequest 인터페이스를 구현한 StandardMultipartHttpServletRequest 를 반환한다.
     * => 이제 컨트롤러에서 HttpServletRequest 대신에 MultipartHttpServletRequest 를 주입받을 수 있는데, 이것을 사용하면 멀티파트와 관련된 여러가지 처리를 편리하게 할 수 있다. 그런데 이후 강의에서 설명할
     * => MultipartFile 이라는 것을 사용하는 것이 더 편하기 때문에 MultipartHttpServletRequest 를 잘사용하지는 않는다
     * <p>
     * false: RequestFacade
     */
    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws
            ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);
        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);
        return "upload-form";
    }
}
