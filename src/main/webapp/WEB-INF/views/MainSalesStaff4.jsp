<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Trang chủ nhân viên</title><link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>"></head>
<body>
<header class="topbar"><a class="brand" href="<c:url value='/staff/home'/>">RestMan</a><div class="user-area"><span>Nhân viên: <strong><c:out value="${sessionScope.fullName}"/></strong></span><a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a></div></header>
<main class="page-shell narrow-shell">
    <section class="panel home-panel"><header class="panel-header"><h1>Trang chủ nhân viên</h1></header><div class="panel-body home-actions"><p>Tìm lượt dùng bàn và xác nhận hóa đơn tại bàn.</p><a class="button button-primary" href="<c:url value='/table-visits'/>">Thanh toán hóa đơn</a></div></section>
</main>
</body>
</html>
