package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.dao.BillDAO4;
import vn.edu.restman.dao.DataAccessException4;
import vn.edu.restman.dao.TableVisitDAO4;
import vn.edu.restman.model.Bill4;
import vn.edu.restman.util.RequestUtil4;

import java.io.IOException;

@WebServlet("/bill-details")
public class BillDetailServlet4 extends HttpServlet {
    private final BillDAO4 billDAO4 = new BillDAO4();
    private final TableVisitDAO4 tableVisitDAO4 = new TableVisitDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer visitId = RequestUtil4.positiveInt(request, "visitId");
        if (visitId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            Bill4 bill = billDAO4.getBillByTableVisit(visitId);
            if (bill == null) {
                restoreVisitList(request);
                request.setAttribute("error", "Chưa có hóa đơn cho lượt dùng bàn đã chọn.");
                request.getRequestDispatcher("/WEB-INF/views/TableVisitSearch4.jsp").forward(request, response);
                return;
            }
            request.setAttribute("bill", bill);
            request.setAttribute("customerName", bill.getCustomerName());
            if (bill.getItems().isEmpty()) request.setAttribute("error", "Chưa có món để thanh toán.");
            request.getRequestDispatcher("/WEB-INF/views/BillDetail4.jsp").forward(request, response);
        } catch (DataAccessException4 exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("errorTitle", "Không tải được hóa đơn");
            request.setAttribute("error", "Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/Error4.jsp").forward(request, response);
        }
    }

    private void restoreVisitList(HttpServletRequest request) {
        String customerName = request.getParameter("customerName");
        request.setAttribute("customerName", customerName);
        request.setAttribute("searched", true);
        if (customerName != null && !customerName.isBlank()) {
            request.setAttribute("tableVisitResults", tableVisitDAO4.findTableVisitsByCustomerName(customerName.trim()));
        }
    }
}
