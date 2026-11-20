package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.dao.DataAccessException4;
import vn.edu.restman.dao.TableVisitDAO4;
import vn.edu.restman.model.TableVisit4;

import java.io.IOException;
import java.util.List;

@WebServlet("/table-visits")
public class TableVisitSearchServlet4 extends HttpServlet {
    private final TableVisitDAO4 tableVisitDAO4 = new TableVisitDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rawCustomerName = request.getParameter("customerName");
        if (rawCustomerName != null) {
            String customerName = rawCustomerName.trim();
            request.setAttribute("customerName", customerName);
            request.setAttribute("searched", true);
            if (customerName.isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập tên khách hàng.");
            } else {
                try {
                    List<TableVisit4> visits = tableVisitDAO4.findTableVisitsByCustomerName(customerName);
                    request.setAttribute("tableVisitResults", visits);
                    if (visits.isEmpty()) request.setAttribute("message", "Không tìm thấy lượt bàn phù hợp.");
                } catch (DataAccessException4 exception) {
                    request.setAttribute("error", "Không thể tìm lượt dùng bàn lúc này. Vui lòng thử lại.");
                }
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/TableVisitSearch4.jsp").forward(request, response);
    }
}
