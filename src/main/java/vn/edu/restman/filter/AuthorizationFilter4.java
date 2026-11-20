package vn.edu.restman.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.security.AuthenticatedUser4;
import vn.edu.restman.security.SessionUtil4;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/customer/*", "/staff/*", "/search-dishes", "/dish-details",
        "/table-visits", "/bill-details", "/process-payment", "/bill-print"
})
public class AuthorizationFilter4 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        AuthenticatedUser4 user = SessionUtil4.getUser(httpRequest);
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        String path = httpRequest.getServletPath();
        boolean customerRoute = path.startsWith("/customer") || path.equals("/search-dishes") || path.equals("/dish-details");
        boolean staffRoute = path.startsWith("/staff") || path.equals("/table-visits")
                || path.equals("/bill-details") || path.equals("/process-payment") || path.equals("/bill-print");
        if ((customerRoute && !user.isCustomer()) || (staffRoute && !user.isSalesStaff())) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        chain.doFilter(request, response);
    }
}
