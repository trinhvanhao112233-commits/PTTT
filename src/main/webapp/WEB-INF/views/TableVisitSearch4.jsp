<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Tìm lượt dùng bàn</title><link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>"></head>
<body>
<header class="topbar"><a class="brand" href="<c:url value='/staff/home'/>">RestMan</a><div class="user-area"><span><c:out value="${sessionScope.fullName}"/></span><a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a></div></header>
<main class="page-shell">
    <section class="panel"><header class="panel-header"><h1>Tìm lượt dùng bàn</h1></header><div class="panel-body">
        <form method="get" action="<c:url value='/table-visits'/>" class="search-form labeled-search" role="search">
            <label for="customerName">Tên khách hàng</label>
            <input id="customerName" name="customerName" type="search" maxlength="150" value="<c:out value='${requestScope.customerName}'/>" placeholder="Ví dụ: Nguyễn Văn B">
            <button class="button button-primary" type="submit">Tìm kiếm</button>
        </form>
        <c:if test="${not empty requestScope.error}"><div class="alert alert-error" role="alert"><c:out value="${requestScope.error}"/></div></c:if>
        <c:if test="${not empty requestScope.tableVisitResults}">
            <div class="table-wrap"><table><caption class="sr-only">Danh sách lượt dùng bàn phù hợp</caption>
                <thead><tr><th>Mã lượt</th><th>Tên khách</th><th>Mã bàn</th><th>Khu vực</th><th class="number">Số khách</th><th>Giờ đến</th><th>Thao tác</th></tr></thead>
                <tbody><c:forEach items="${requestScope.tableVisitResults}" var="visit"><tr>
                    <td><c:out value="${visit.id}"/></td><td><c:out value="${visit.reservation.customer.fullName}"/></td>
                    <td><c:out value="${visit.table.tableCode}"/></td><td><c:out value="${visit.table.area}"/></td>
                    <td class="number"><c:out value="${visit.guestCount}"/></td><td><c:out value="${visit.arrivalTimeDisplay}"/></td>
                    <td><c:url value="/bill-details" var="billUrl"><c:param name="visitId" value="${visit.id}"/><c:param name="customerName" value="${requestScope.customerName}"/></c:url><a href="${billUrl}">Xem hóa đơn</a></td>
                </tr></c:forEach></tbody>
            </table></div>
        </c:if>
        <c:if test="${requestScope.searched and empty requestScope.tableVisitResults and empty requestScope.error}"><div class="empty-state">Không tìm thấy lượt bàn phù hợp.</div></c:if>
        <div class="actions"><a class="button button-secondary" href="<c:url value='/staff/home'/>">Quay lại</a></div>
    </div></section>
</main>
</body>
</html>
