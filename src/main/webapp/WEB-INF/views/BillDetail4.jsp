<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Chi tiết hóa đơn</title><link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>"></head>
<body>
<header class="topbar"><a class="brand" href="<c:url value='/staff/home'/>">RestMan</a><div class="user-area"><span><c:out value="${sessionScope.fullName}"/></span><a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a></div></header>
<main class="page-shell detail-shell">
    <section class="panel"><header class="panel-header"><h1>Chi tiết hóa đơn</h1></header><div class="panel-body">
        <c:if test="${not empty requestScope.error}"><div class="alert alert-error" role="alert"><c:out value="${requestScope.error}"/></div></c:if>
        <c:if test="${not empty requestScope.bill}">
            <div class="bill-heading"><h2>Hóa đơn <c:out value="${bill.id}"/> <span>•</span> Bàn <c:out value="${bill.tableCode}"/> <span>•</span> Lượt <c:out value="${bill.visit.id}"/></h2><p>Trạng thái: <strong><c:out value="${bill.status}"/></strong></p></div>
            <c:choose>
                <c:when test="${not empty bill.items}">
                    <div class="table-wrap"><table><thead><tr><th>Tên món</th><th class="number">SL</th><th class="number">Đơn giá</th><th class="number">Thành tiền</th></tr></thead><tbody>
                    <c:forEach items="${bill.items}" var="item"><tr><td><c:out value="${item.dish.name}"/></td><td class="number"><c:out value="${item.quantity}"/></td><td class="number"><fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0"/> đ</td><td class="number"><fmt:formatNumber value="${item.lineAmount}" type="number" maxFractionDigits="0"/> đ</td></tr></c:forEach>
                    </tbody></table></div>
                    <p class="bill-total">Tổng tiền: <fmt:formatNumber value="${bill.totalAmount}" type="number" maxFractionDigits="0"/> đ</p>
                </c:when>
                <c:otherwise><c:if test="${empty requestScope.error}"><div class="alert alert-error" role="alert">Chưa có món để thanh toán.</div></c:if></c:otherwise>
            </c:choose>
            <div class="actions actions-between">
                <c:url value="/table-visits" var="visitsUrl"><c:param name="customerName" value="${bill.customerName}"/></c:url>
                <a class="button button-secondary" href="${visitsUrl}">Quay lại</a>
                <c:if test="${bill.status eq 'Chưa thanh toán' and not empty bill.items}">
                    <form method="post" action="<c:url value='/process-payment'/>" class="inline-form">
                        <input type="hidden" name="billId" value="<c:out value='${bill.id}'/>">
                        <input type="hidden" name="visitId" value="<c:out value='${bill.visit.id}'/>">
                        <input type="hidden" name="customerName" value="<c:out value='${bill.customerName}'/>">
                        <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
                        <button class="button button-primary" type="submit">Xác nhận thanh toán</button>
                    </form>
                </c:if>
                <c:if test="${bill.paid}">
                    <c:url value="/bill-print" var="printUrl"><c:param name="visitId" value="${bill.visit.id}"/></c:url>
                    <a class="button button-primary" href="${printUrl}">Mở bản in</a>
                </c:if>
            </div>
        </c:if>
    </div></section>
</main>
</body>
</html>
