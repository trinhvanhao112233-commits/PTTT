package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.dao.BillDAO4;
import vn.edu.restman.model.Bill4;
import vn.edu.restman.util.RequestUtil4;

import java.io.IOException;

@WebServlet("/bill-print")
public class BillPrintServlet4 extends HttpServlet {
    private final BillDAO4 billDAO4 = new BillDAO4();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer visitId = RequestUtil4.positiveInt(request, "visitId");
        if (visitId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Bill4 bill = billDAO4.getBillByTableVisit(visitId);
        if (bill == null || !bill.isPaid()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ in hóa đơn đã thanh toán.");
            return;
        }
        request.setAttribute("bill", bill);
        request.getRequestDispatcher("/WEB-INF/views/BillPrint4.jsp").forward(request, response);
    }
}
