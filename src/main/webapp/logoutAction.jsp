<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width; initial-scale=1">
<title>JSP Ajax 실시간 채팅 서비스</title>
</head>
<body>
	<%
	// 로그아웃시 ~~~~~~ 작동하는 페이지
	// session 정보 지우기
	session.invalidate();
	%>
	<script>
		// 메인 페이지로 이동
		location.href = 'index.jsp';
	</script>
</body>
</html>