<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tìm kiếm món ăn</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>">
</head>
<body>
<header class="topbar">
    <a class="brand" href="<c:url value='/customer/home'/>">RestMan</a>
    <div class="user-area"><span><c:out value="${sessionScope.fullName}"/></span><a class="text-link" href="<c:url value='/logout'/>">Đăng xuất</a></div>
</header>
<main class="page-shell">
    <section class="panel">
        <header class="panel-header"><h1>Tìm kiếm món ăn</h1></header>
        <div class="panel-body">
            <form method="get" action="<c:url value='/search-dishes'/>" class="search-form" role="search">
                <label class="sr-only" for="keyword">Tên món</label>
                <input id="keyword" name="keyword" type="search" maxlength="150"
                       value="<c:out value='${requestScope.keyword}'/>" placeholder="Nhập toàn bộ hoặc một phần tên món">
                <button class="button button-primary" type="submit">Tìm kiếm</button>
            </form>

            <c:if test="${not empty requestScope.error}">
                <div class="alert alert-error" role="alert"><c:out value="${requestScope.error}"/></div>
            </c:if>

            <c:if test="${not empty requestScope.dishResults}">
                <div class="table-wrap">
                    <table>
                        <caption class="sr-only">Danh sách món ăn phù hợp</caption>
                        <thead><tr><th>Mã món</th><th>Tên món</th><th class="number">Giá bán</th><th>Đơn vị tính</th><th>Tình trạng</th><th>Thao tác</th></tr></thead>
                        <tbody>
                        <c:forEach items="${requestScope.dishResults}" var="dish">
                            <tr>
                                <td><c:out value="${dish.dishCode}"/></td>
                                <td><c:out value="${dish.name}"/></td>
                                <td class="number"><fmt:formatNumber value="${dish.salePrice}" type="number" maxFractionDigits="0"/> đ</td>
                                <td><c:out value="${dish.unit}"/></td>
                                <td><span class="status ${dish.servingStatus eq 'Tạm hết' ? 'status-warning' : 'status-success'}"><c:out value="${dish.servingStatus}"/></span></td>
                                <td>
                                    <c:url value="/dish-details" var="detailUrl">
                                        <c:param name="dishCode" value="${dish.dishCode}"/>
                                        <c:param name="keyword" value="${requestScope.keyword}"/>
                                    </c:url>
                                    <a href="${detailUrl}">Xem chi tiết</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${requestScope.searched and empty requestScope.dishResults and empty requestScope.error}">
                <div class="empty-state">Không tìm thấy món phù hợp.</div>
            </c:if>

            <div class="actions"><a class="button button-secondary" href="<c:url value='/customer/home'/>">Quay lại</a></div>
        </div>
    </section>
</main>
</body>
</html>
