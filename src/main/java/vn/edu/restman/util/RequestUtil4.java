package vn.edu.restman.util;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestUtil4 {
    private RequestUtil4() { }

    public static Integer positiveInt(HttpServletRequest request, String parameterName) {
        String rawValue = request.getParameter(parameterName);
        if (rawValue == null || rawValue.isBlank()) return null;
        try {
            int value = Integer.parseInt(rawValue);
            return value > 0 ? value : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
