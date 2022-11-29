<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%
	String userID = null;
	if(session.getAttribute("userID") != null){
		userID = (String)session.getAttribute("userID");
	}
	// 채팅 시스템 매개변수로 보낼 toID 설정
	String toID = null;
	if(request.getParameter("toID") != null){
		toID = (String)request.getParameter("toID");
	}
	// 비로그인 상태일 때
	if(userID == null) {
		%>
		<script>
			alert("현재 로그인이 되어 있지 않습니다.");
			document.location.href="index.jsp";
		</script>
		<%
		//response.sendRedirect("index.jsp");
		return;
	} 
	if(toID == null) {
		%>
		<script>
			alert("대화 상대가 지정되지 않았습니다.");
			document.location.href="index.jsp";
		</script>
		<%
		return;
	}
	%>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width; initial-scale=1">
<%--
 <link rel="stylesheet" href="css/bootstrap.css">
 --%>
<link rel="stylesheet" href="css/custom.css">
<link rel="stylesheet" href="css/indexStyle.css">
<title>JSP Ajax 실시간 채팅 서비스</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.js"></script>
<%--
 --%>
 <%
 request.setCharacterEncoding("utf-8");
 %>
 <script type="text/javascript">
 	// 메세지 전송 성공 여부 알려주는 팝업창 띄우는 함수
 	function autoClosingAlert(selector, delay) {
 		var alert = $(selector).alert();
 		alert.show();// 팝업창처럼 보여지도록 만들어주기
 		window.setTimeout(function(){ alert.hide() }, delay); // delay만큼의 시간동안만 보여지도록
 	}
 	// 메세지를 보내는 함수
 	function submitFunction(){
 		var fromID = '<%= userID %>';
 		var toID = '<%= toID %>';
 		var chatContent = $('#chatContent').val();
 		// 비동기적 통신 (ajax)
 		$.ajax({
 			type: "POST",
 			url: "./chatSubmitServlet",
 			data: {
 				fromID: encodeURIComponent(fromID),
 				toID: encodeURIComponent(toID),
 				chatContent: encodeURIComponent(chatContent),
 			},
 			success: function(result){
 				if(result == 1){ // 성공적으로 메세지를 보냈다면
 					// autoClosingAlert 함수 실행
 					autoClosingAlert('#successMessage', 2000);
 				} else if(result == 0){
 					autoClosingAlert('#dangerMessage', 2000);
 				} else {
 					autoClosingAlert('#warningMessage', 2000);
 				}
 			}
 		});
		// chatContent 값 비워주기 (chatContent : 보낼 메세지 적는 칸)
		$('#chatContent').val('');
 	}
 	var lastID = 0; // 가장 마지막 채팅의 chatID값
 	function chatListFunction(type){
 		var fromID = '<%= userID %>';
 		var toID = '<%= toID %>';
 		$.ajax({
 			type: "POST",
 			url: "./chatListServlet",
 			data: { // 한글로 작성했을 경우를 대비 -> encodeURI 사용
 				fromID: encodeURIComponent(fromID),
 				toID: encodeURIComponent(toID),
 				listType: type
 			},
 			success: function(data){ // 성공적으로 통신을 수행했다면 -> data에 json형식의 데이터 담김
 				if(data == "") return; // 공백일 경우 제외
 				var parsed = JSON.parse(data); // json형태로 파싱
 				var result = parsed.result; // result에 담아주기
 				for(var i=0; i<result.length; i++){
 					// 메세지 출력
 					addChat(result[i][0].value, result[i][2].value, result[i][3].value);
 				}
 			// ChatListServlet의 getID함수에서 last 키에 해당하는 값 (chatID값)
 				lastID = Number(parsed.last); 
 			}
 		});
 	}
	// 매개변수 = 챗보낸사람, 챗내용, 챗시간
	function addChat(chatName, chatContent, chatTime){
		// 채팅 리스트에 가져온 데이터 정보 담기
		$("#chatList").append('<div class="row">' +
			'<div class="col-lg-12">' +
			'<div class="media"' +
			'<a class="bull-left" href="#">' +
			'<img class="media-object img-circle" style="width: 30px; height: 30px;" src="images/icon.png" alt="">' +
			'</a>' +
			'<div class="media-body">' +
			'<h4 class="media-heading">' +
			chatName +
			'<span class="small pull-right">' +
			chatTime +
			'</span>' +
			'</h4>' +
			'<p>' +
			chatContent +
			'</p>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'</div>' +
			'<hr>');
		// 스크롤 가장 아래로 내려주기
		$('#chatList').scrollTop($('#chatList')[0].scrollHeight);
	}
	// 3초 간격으로 새로운 채팅 내용 가져오기
	function getInfiniteChat(){
		setInterval(function(){
			chatListFunction(lastID);
		}, 3000);
	}
 </script>
</head>
<body>
	
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false"> 버튼
				<%--
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				--%>
			</button>
			<a class="navber-brand" href="index.jsp">실시간 채팅 서비스</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="index.jsp">메인</a>
			</ul>
			<%
			// 로그인 상태
			if(userID != null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="buton" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%		
				}
			%>
		</div>
	</nav>
	<div class="container bootstrap snippet" id="chatWrap">
		<div class="row">
			<div class="col-xs-12">
				<div class="portlet portlet-default">
					<div class="portlet-title">
						<h4><i class="fa fa-circle text-green"></i>실시간 채팅창</h4>
					</div>
					<div class="clearfix"></div>
				</div>
				<div id="chat" class="panel-collapse collapse in">
					<div id="chatList" class="porlet-body chat-widget" style="overflow-y: auto; width:auto; height:600px;">
					</div>
					<div class="portlet-footer">
						<div class="row" style="height: 90px;">
							<div class="form-group col-xs-10">
								<textarea style="height: 80px;" id="chatContent" class="form-control" placeholder="메시지를 입력하세요." maxlength="100"></textarea>
							</div>
							<div class="form-group col-xs-2">
								<button type="button" class="btn btn-default pull-right" onclick="submitFunction()">전송</button>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="alert alert-success" id="successMessage" style="display: none;">
		<strong>메세지 전송에 성공했습니다.</strong>
	</div>
	<div class="alert alert-danger" id="dangerMessage" style="display: none;">
		<strong>이름과 내용을 모두 입력해주세요</strong>
	</div>
	<div class="alert alert-warning" id="warningMessage" style="display: none;">
		<strong>데이터베이스 오류가 발생했습니다.</strong>
	</div>
	<script type="text/javascript">
		// 문서가 다 불러와 졌을 때!
		$(document).ready(function(){
			chatListFunction('ten');
			getInfiniteChat();
		})
	</script>
</body>
</html>