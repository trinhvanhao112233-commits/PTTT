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
import java.util.List;

@WebServlet("/search-dishes")
public class SearchDishServlet4 extends HttpServlet {
    private final DishDAO4 dishDAO4 = new DishDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rawKeyword = request.getParameter("keyword");
        if (rawKeyword != null) {
            String keyword = rawKeyword.trim();
            request.setAttribute("keyword", keyword);
            request.setAttribute("searched", true);
            if (keyword.isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập tên món.");
            } else {
                try {
                    List<Dish4> dishes = dishDAO4.searchDishesByName(keyword);
                    request.setAttribute("dishResults", dishes);
                    if (dishes.isEmpty()) request.setAttribute("message", "Không tìm thấy món phù hợp.");
                } catch (DataAccessException4 exception) {
                    request.setAttribute("error", "Không thể tìm món lúc này. Vui lòng thử lại.");
                }
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/SearchDish4.jsp").forward(request, response);
    }
}
