package vn.edu.restman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.restman.dao.BillDAO4;
import vn.edu.restman.dao.DataAccessException4;
import vn.edu.restman.dao.PaymentException4;
import vn.edu.restman.model.Bill4;
import vn.edu.restman.security.CsrfUtil4;
import vn.edu.restman.security.SessionUtil4;
import vn.edu.restman.util.RequestUtil4;

import java.io.IOException;

@WebServlet("/process-payment")
public class ProcessPaymentServlet4 extends HttpServlet {
    private final BillDAO4 billDAO4 = new BillDAO4();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtil4.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Yêu cầu xác nhận không hợp lệ.");
            return;
        }
        Integer billId = RequestUtil4.positiveInt(request, "billId");
        Integer visitId = RequestUtil4.positiveInt(request, "visitId");
        Integer staffId = SessionUtil4.getStaffId(request);
        if (billId == null || visitId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (staffId == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            boolean confirmed = billDAO4.confirmPayment(billId, staffId);
            if (!confirmed) {
                showFailure(request, response, visitId, "Không thể xác nhận thanh toán.");
                return;
            }
            Bill4 paidBill = billDAO4.getBillByTableVisit(visitId);
            if (paidBill == null || paidBill.getId() != billId || !paidBill.isPaid()) {
                throw new DataAccessException4("Khong tai lai duoc hoa don da thanh toan");
            }
            request.setAttribute("bill", paidBill);
            request.getRequestDispatcher("/WEB-INF/views/BillPrint4.jsp").forward(request, response);
        } catch (PaymentException4 exception) {
            String message = switch (exception.getReason()) {
                case EMPTY_BILL -> "Chưa có món để thanh toán.";
                case ALREADY_CONFIRMED -> "Hóa đơn đã được xác nhận trước đó, không thể xác nhận trùng.";
                case NOT_OPEN -> "Hóa đơn không còn ở trạng thái Chưa thanh toán.";
                case INVALID_STAFF -> "Tài khoản hiện tại không có quyền xác nhận hóa đơn.";
                case NOT_FOUND -> "Không tìm thấy hóa đơn cần thanh toán.";
            };
            showFailure(request, response, visitId, message);
        } catch (DataAccessException4 exception) {
            showFailure(request, response, visitId, "Không thể xác nhận thanh toán do lỗi dữ liệu. Vui lòng thử lại.");
        }
    }

    private void showFailure(HttpServletRequest request, HttpServletResponse response, int visitId, String message)
            throws ServletException, IOException {
        Bill4 bill = billDAO4.getBillByTableVisit(visitId);
        request.setAttribute("bill", bill);
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/BillDetail4.jsp").forward(request, response);
    }
}
