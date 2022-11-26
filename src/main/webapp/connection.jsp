
<html>
<head>
<%@ page import="java.sql.*, javax.sql.*, java.io.*, javax.naming.InitialContext, javax.naming.Context" %>
</head>
<body>
	<%-- 실제로 커넥션풀에 접근가능하도록 설정해주기 --%>
	<%
	// 객체 생성
	InitialContext initCtx = new InitialContext();
	//initCtx를 중심으로 리소스를 찾을 수 있도록
	Context envContext = (Context)initCtx.lookup("java:/comp/env");
	// 실제로 소스를 발견하게되면 우리 프로젝트에 접근할 수 있도록
	DataSource ds = (DataSource)envContext.lookup("jdbc/UserChat");
	// connection 객체를 이용해서 실제로 데이터베이스에 접근할 수 있도록
	 //context.xml 파일에 있는 mysql url을 이용해서 해당 주소에 실제로 접근할 수 있도록~
	Connection conn = ds.getConnection();
	// Statement : 실제로 어떤 sql문장을 db에 입력한 다음 -> 그 결과를 반환시켜주는 역할
	Statement stmt = conn.createStatement();
	// 쿼리문의 결과를 rset에 저장
	ResultSet rset = stmt.executeQuery("select version();");
	// 만약 결과가 존재한다면
	while(rset.next()){
		out.println("MySQL Version: " + rset.getString("version()"));
	}
	rset.close();
	stmt.close();
	conn.close();
	initCtx.close();
	%>
</body>
</html>