<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi tiết món ăn</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>">
</head>
<body>
<header class="topbar"><a class="brand" href="<c:url value='/customer/home'/>">RestMan</a><div class="user-area"><span><c:out value="${sessionScope.fullName}"/></span><a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a></div></header>
<main class="page-shell detail-shell">
    <section class="panel">
        <header class="panel-header"><h1>Chi tiết món ăn</h1></header>
        <div class="panel-body">
            <c:choose>
                <c:when test="${not empty requestScope.dish}">
                    <dl class="detail-grid">
                        <dt>Mã món</dt><dd><c:out value="${dish.dishCode}"/></dd>
                        <dt>Tên món</dt><dd><c:out value="${dish.name}"/></dd>
                        <dt>Danh mục</dt><dd><c:out value="${dish.category}"/></dd>
                        <dt>Loại món</dt><dd><c:out value="${dish.dishType}"/></dd>
                        <dt>Đơn vị tính</dt><dd><c:out value="${dish.unit}"/></dd>
                        <dt>Giá bán</dt><dd><fmt:formatNumber value="${dish.salePrice}" type="number" maxFractionDigits="0"/> đ</dd>
                        <dt>Tình trạng phục vụ</dt><dd><span class="status ${dish.servingStatus eq 'Tạm hết' ? 'status-warning' : 'status-success'}"><c:out value="${dish.servingStatus}"/></span></dd>
                        <dt>Mô tả</dt><dd><c:out value="${dish.description}" default="Chưa có mô tả."/></dd>
                    </dl>
                </c:when>
                <c:otherwise><div class="alert alert-error" role="alert"><c:out value="${requestScope.error}" default="Không tìm thấy món ăn."/></div></c:otherwise>
            </c:choose>
            <c:url value="/search-dishes" var="backUrl"><c:param name="keyword" value="${requestScope.keyword}"/></c:url>
            <div class="actions"><a class="button button-secondary" href="${backUrl}">Quay lại</a></div>
        </div>
    </section>
</main>
</body>
</html>
