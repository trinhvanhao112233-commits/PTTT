package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.restman.dao.AuthDAO4;
import vn.edu.restman.security.AuthenticatedUser4;
import vn.edu.restman.security.CsrfUtil4;
import vn.edu.restman.security.SessionUtil4;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet4 extends HttpServlet {
    private final AuthDAO4 authDAO4 = new AuthDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuthenticatedUser4 existing = SessionUtil4.getUser(request);
        if (existing != null) {
            redirectHome(request, response, existing);
            return;
        }
        CsrfUtil4.ensureToken(request.getSession(true));
        request.getRequestDispatcher("/WEB-INF/views/Login4.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtil4.isValid(request)) {
            request.setAttribute("error", "Phiên đăng nhập không hợp lệ. Vui lòng thử lại.");
            doGet(request, response);
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập và mật khẩu.");
            request.setAttribute("username", username);
            doGet(request, response);
            return;
        }
        try {
            AuthenticatedUser4 user = authDAO4.authenticate(username, password);
            if (user == null) {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                request.setAttribute("username", username);
                doGet(request, response);
                return;
            }
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("authUser", user);
            session.setAttribute("userId", user.userId());
            session.setAttribute("staffId", user.staffId());
            session.setAttribute("role", user.role());
            session.setAttribute("fullName", user.fullName());
            CsrfUtil4.ensureToken(session);
            redirectHome(request, response, user);
        } catch (IllegalStateException exception) {
            request.setAttribute("error", "Không thể kết nối cơ sở dữ liệu. Vui lòng kiểm tra cấu hình MySQL.");
            request.setAttribute("username", username);
            doGet(request, response);
        }
    }

    private void redirectHome(HttpServletRequest request, HttpServletResponse response, AuthenticatedUser4 user) throws IOException {
        String home = user.isSalesStaff() ? "/staff/home" : "/customer/home";
        response.sendRedirect(request.getContextPath() + home);
    }
}
