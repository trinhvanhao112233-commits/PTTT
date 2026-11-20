<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang chủ khách hàng</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/customer/home'/>">RestMan</a>
    <div class="user-area">
        <span>Xin chào, <strong><c:out value="${sessionScope.fullName}"/></strong></span>
        <a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a>
    </div>
</header>
<main class="page-shell narrow-shell">
    <section class="panel home-panel">
        <header class="panel-header"><h1>Trang chủ khách hàng</h1></header>
        <div class="panel-body home-actions">
            <p>Tra cứu món ăn theo toàn bộ hoặc một phần tên món.</p>
            <a class="button button-primary" href="<c:url value='/search-dishes'/>">Tìm kiếm thông tin món ăn</a>
        </div>
    </section>
</main>
</body>
</html>
