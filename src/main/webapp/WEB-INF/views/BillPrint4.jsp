<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Hóa đơn đã thanh toán</title><link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>"></head>
<body class="print-page">
<main class="page-shell receipt-shell">
    <section class="panel receipt"><header class="panel-header"><p class="eyebrow">RESTMAN</p><h1>Hóa đơn đã thanh toán</h1></header><div class="panel-body">
        <div class="bill-heading"><h2>Hóa đơn <c:out value="${bill.id}"/> <span>•</span> Bàn <c:out value="${bill.tableCode}"/></h2><p>Khách hàng: <c:out value="${bill.customerName}"/></p><p>Trạng thái: <strong><c:out value="${bill.status}"/></strong></p><c:if test="${not empty bill.confirmedAtDisplay}"><p>Xác nhận lúc: <c:out value="${bill.confirmedAtDisplay}"/></p></c:if></div>
        <div class="table-wrap"><table><thead><tr><th>Tên món</th><th class="number">SL</th><th class="number">Đơn giá</th><th class="number">Thành tiền</th></tr></thead><tbody>
            <c:forEach items="${bill.items}" var="item"><tr><td><c:out value="${item.dish.name}"/></td><td class="number"><c:out value="${item.quantity}"/></td><td class="number"><fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0"/> đ</td><td class="number"><fmt:formatNumber value="${item.lineAmount}" type="number" maxFractionDigits="0"/> đ</td></tr></c:forEach>
        </tbody></table></div>
        <p class="bill-total">Tổng tiền: <fmt:formatNumber value="${bill.totalAmount}" type="number" maxFractionDigits="0"/> đ</p>
        <p class="receipt-thanks">Cảm ơn quý khách và hẹn gặp lại.</p>
        <div class="actions no-print"><button class="button button-primary" type="button" onclick="window.print()">In hóa đơn</button><a class="button button-secondary" href="<c:url value='/staff/home'/>">Về trang chủ</a></div>
    </div></section>
</main>
</body>
</html>
