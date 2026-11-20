<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Không thể xử lý yêu cầu</title><link rel="stylesheet" href="<c:url value='/assets/css/restman.css'/>"></head>
<body><main class="page-shell narrow-shell"><section class="panel error-panel"><header class="panel-header"><h1><c:out value="${requestScope.errorTitle}" default="Không thể xử lý yêu cầu"/></h1></header><div class="panel-body"><div class="alert alert-error" role="alert"><c:out value="${requestScope.error}" default="Hệ thống gặp lỗi. Vui lòng thử lại."/></div><div class="actions"><a class="button button-primary" href="<c:url value='/'/>">Về trang đăng nhập</a></div></div></section></main></body>
</html>
