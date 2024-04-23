package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("login preHandle start");
        //HTTP Session 가져오기
        HttpSession session = request.getSession(false);

        //HTTP Session 이 없거나, LOGIN_MEMBER 값이 없다면
        // /login 으로 리다이렉션 & 이후 과정 진행 X
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            response.sendRedirect("/login?requestURL=" + request.getRequestURI());
            return false;
        }
        //이후 과정 진행
        return true;
    }
}
