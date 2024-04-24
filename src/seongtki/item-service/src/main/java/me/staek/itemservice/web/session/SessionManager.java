package me.staek.itemservice.web.session;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(cookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request);
        if (cookie == null)
            return null;
        return sessionStore.get(cookie.getValue());
    }

    private static Cookie findCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        Cookie cookie = Arrays.stream(cookies)
                                .filter(f -> SESSION_COOKIE_NAME.equals(f.getName()))
                                .findAny().orElse(null);
        return cookie;
    }

    public void expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request);
        if (cookie != null)
            sessionStore.remove(cookie.getValue());
    }
}
