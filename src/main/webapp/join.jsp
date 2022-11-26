<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width; initial-scale=1">
<%--
<link rel="stylesheet" href="./css/bootstrap.css">
 --%>
<link rel="stylesheet" href="./css/custom.css">
<title>JSP Ajax 실시간 채팅 서비스</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="./js/memeber_insert.js" defer></script>
<script type="text/javascript">
	function registerCheckFunction(){
		var userID = $('#userID').val(); // userID 요소의 value값을 가져오기
		// 비동기통신
		$.ajax({
			type: 'POST', // post방식으로 보내기(보안성)
			url: './UserRegisterCheckServlet', // 값을 보낼 url 설정
			data: {userID: userID}, //보내는값(파라미터변수로 userID 보내기), 속성이름 : 왼, 값: 오
			success: function(result){ // 성공적으로 data를 보냈다면 -> result에 반환값이 담김
				if(result == 1){ // 성공적으로 사용할 수 있다는 메세지
					console.log("test");
					alert("사용가능한 아이디입니다.");
					//$('#checkMessage').html('사용할 수 있는 아이디입니다.');
					//$('#checkType').attr('class', 'modal-content panel-success');
				} else {
					alert("사용할 수 없는 아이디입니다.");
					//$('#checkMessage').html('사용할 수 없는 아이디입니다.');
					//$('#checkType').attr('class', 'modal-content panel-warning');
				}
				//$('#checkModal').modal("show"); // modal이 보이도록 설정
			}
		});
	}
	function passwordCheckFunction(){
		var userPassword1 = $('#userPassword1').val();
		var userPassword2 = $('#userPassword2').val();
		// 비번, 비번확인 일치여부
		if(userPassword1 != userPassword2){ // 일치하지않으면
			$('#passwordCheckMessage').html('비밀번호가 서로 일치하지 않습니다.');
		} else { //일치하면(메세지 비우기)
			$('#passwordCheckMessage').html('')
		}
	}
</script>
</head>
<body>
	<%
		String userID = null;
	if(session.getAttribute("userID") != null){
		userID = (String)session.getAttribute("userID");
	}
	%>
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
				if(userID == null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="buton" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>
			</ul>
			<%
				} else {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="buton" aria-haspopup="true"
						aria-expanded="false">회원가입<span class="caret"></span>
					</a>
					</ul>
				</li>
			</ul>
			<%		
					
				}
			%>
		</div>
	</nav>
	<div class="container">
		<form method="post" action="./userRegister" name="member_insert">
			<table class="table table-bordered table-hover" style="text-align: center; border:1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3"><h4>회원 등록 양식</h4></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width:110px;"><h5>아이디</h5></td>
						<td><input class="form-control" type="text" id="userID" name="userID" maxlength="20" placeholder="아이디를 입력하세요"></td>
						<td style="width:110px;"><button class="btn btn-primary" onclick="registerCheckFunction();" type="button">즁복체크</button></td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>비밀번호</h5></td>
						<td colspan="2"><input onkeyup="passwordCheckFunction()" class="form-control" id="userPassword1" type="password" name="userPassword1" maxlength="20" placeholder="비밀번호를 입력하세요"></td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>비밀번호 확인</h5></td>
						<td colspan="2"><input onkeyup="passwordCheckFunction()" class="form-control" id="userPassword2" type="password" name="userPassword2" maxlength="20" placeholder="비밀번호 확인을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>이름</h5></td>
						<td colspan="2"><input class="form-control" id="userName" type="text" name="userName" maxlength="20" placeholder="이름을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>나이</h5></td>
						<td colspan="2"><input class="form-control" id="userAge" type="number" name="userAge" maxlength="20" placeholder="나이를 입력하세요"></td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>성별</h5></td>
						<td colspan="2">
							<div class="form-group" syle="text-align: center; margin: 0 auto;">
								<label class="btn btn-primary active">
									<input type="radio" name="userGender" autocomplete="off" value="남자" checked>남자
								</label>
								<label class="btn btn-primary">
									<input type="radio" name="userGender" autocomplete="off" value="여자">여자
								</label>
							</div>
						</td>
					</tr>
					<tr>
						<td style="width:110px;"><h5>이메일</h5></td>
						<td colspan="2"><input class="form-control" id="userEmail" type="email" name="userEmail" maxlength="20" placeholder="이메일을 입력하세요"></td>
					</tr>
					<tr>
						<td style="text-align: left;" colspan="3"><h5 style="color: red;" id="passwordCheckMessage"></h5>
						<input class="btn btn-primary pull-right" type="button" value="등록" onclick="insert()">
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
<%--
	<% // session값 검증하기 (UserRegisterServlet으로 부터 받은 값!!)
	
		// modal 창 생성
		String messageContent = null;
		// session에 messageContent값이 비어있지 않은 경우
		if(session.getAttribute("messageContent") != null){
			// session의 messageContent값을 변수 messageContent에 담아주기
			messageContent = (String)session.getAttribute("messageContent");
		}
		
		String messageType = null;
		// session에 messageType 비어있지 않은 경우
		if(session.getAttribute("messageType") != null){
			// session의 messageType 변수 messageType 담아주기
			messageType = (String)session.getAttribute("messageType");
		}
		
		// messageContent값이 존재하는 경우
		if(messageContent != null){
	%>

	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<!-- messageType에 담긴 값(오류or성공메세지)에 따른 클래스 전환?바꿈? -->
				<div class="modal-content <% if(messageType.equals("오류 메세지")){out.println("panel-warning");} else {out.println("panel-success");}%>">
					<div class="modal-header panel-heading">
						<!-- 닫기버튼 -->
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span> <!-- x -->
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%= messageType %>
						</h4>
					</div>
					<div class="modal-body">
						<%= messageContent %>
					</div>
					<div class="modal-footer"><!-- 닫기버튼 -->
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		// modal창 보이도록
		$('#messageModal').modal("show");
	</script>

	<% // 단 한번만 메세지창 보이도록 -> removeAttribute하기
		session.removeAttribute("messageContent");
		session.removeAttribute("messgageType");
		}
	%>
	
 --%>
<%--
	<!-- 아이디 중복체크 -->
	<!-- 정보 띄워주는 modal 팝업창 -->
	<div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div id="checkType" class="modal-content panel-info">
					<div class="modal-header panel-heading">
						<!-- 닫기버튼 -->
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span> <!-- x -->
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							확인 메세지
						</h4>
					</div>
					<!-- checkMessage => 사용할 수 있는/없는 아이디인지 알려주는 메세지 출력 -->
					<div id="checkMessage" class="modal-body">
					</div>
					<div class="modal-footer"><!-- 닫기버튼 -->
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
 --%>
</body>
</html>