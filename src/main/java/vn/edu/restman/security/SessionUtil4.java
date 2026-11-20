package vn.edu.restman.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil4 {
    private SessionUtil4() { }

    public static AuthenticatedUser4 getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object value = session.getAttribute("authUser");
        return value instanceof AuthenticatedUser4 user ? user : null;
    }

    public static Integer getStaffId(HttpServletRequest request) {
        AuthenticatedUser4 user = getUser(request);
        return user == null ? null : user.staffId();
    }
}
