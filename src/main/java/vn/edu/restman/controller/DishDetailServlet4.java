package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.dao.DataAccessException4;
import vn.edu.restman.dao.DishDAO4;
import vn.edu.restman.model.Dish4;

import java.io.IOException;

@WebServlet("/dish-details")
public class DishDetailServlet4 extends HttpServlet {
    private final DishDAO4 dishDAO4 = new DishDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dishCode = request.getParameter("dishCode");
        if (dishCode == null || dishCode.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            Dish4 dish = dishDAO4.getDishDetails(dishCode.trim());
            if (dish == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorTitle", "Không tìm thấy món ăn");
                request.setAttribute("error", "Mã món đã chọn không tồn tại.");
                request.getRequestDispatcher("/WEB-INF/views/Error4.jsp").forward(request, response);
                return;
            }
            request.setAttribute("dish", dish);
            request.setAttribute("keyword", request.getParameter("keyword"));
            request.getRequestDispatcher("/WEB-INF/views/DishDetail4.jsp").forward(request, response);
        } catch (DataAccessException4 exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("errorTitle", "Không tải được chi tiết món");
            request.setAttribute("error", "Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/Error4.jsp").forward(request, response);
        }
    }
}
