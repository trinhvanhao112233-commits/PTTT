<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập RestMan</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>">
</head>
<body class="login-page">
<main class="login-shell">
    <section class="panel login-card" aria-labelledby="login-title">
        <header class="panel-header">
            <p class="eyebrow">RESTMAN · ĐỀ 20</p>
            <h1 id="login-title">Đăng nhập hệ thống</h1>
        </header>
        <div class="panel-body">
            <p class="muted">Dùng tài khoản khách hàng để tìm món hoặc tài khoản nhân viên bán hàng để thanh toán.</p>

            <c:if test="${not empty requestScope.error}">
                <div class="alert alert-error" role="alert"><c:out value="${requestScope.error}"/></div>
            </c:if>
            <c:if test="${param.loggedOut eq '1'}">
                <div class="alert alert-success" role="status">Bạn đã đăng xuất.</div>
            </c:if>

            <form method="post" action="<c:url value='/login'/>" class="stack-form">
                <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>"/>
                <label for="username">Tên đăng nhập</label>
                <input id="username" name="username" type="text" value="<c:out value='${requestScope.username}'/>"
                       autocomplete="username" maxlength="50" required autofocus>

                <label for="password">Mật khẩu</label>
                <input id="password" name="password" type="password" autocomplete="current-password" required>

                <button class="button button-primary button-block" type="submit">Đăng nhập</button>
            </form>

            <div class="demo-accounts" aria-label="Tài khoản demo">
                <h2>Tài khoản demo</h2>
                <p><strong>Khách hàng:</strong> khachhang / customer123</p>
                <p><strong>Nhân viên:</strong> nhanvien / staff123</p>
            </div>
        </div>
    </section>
</main>
</body>
</html>
