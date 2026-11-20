package vn.edu.restman.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public final class CsrfUtil4 {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String SESSION_KEY = "csrfToken";

    private CsrfUtil4() { }

    public static String ensureToken(HttpSession session) {
        Object existing = session.getAttribute(SESSION_KEY);
        if (existing instanceof String token && !token.isBlank()) return token;
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setAttribute(SESSION_KEY, token);
        return token;
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Object expected = session.getAttribute(SESSION_KEY);
        String actual = request.getParameter(SESSION_KEY);
        return expected instanceof String token && token.equals(actual);
    }
}
