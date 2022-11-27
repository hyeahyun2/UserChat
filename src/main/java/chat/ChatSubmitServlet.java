package chat;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChatSubmitServlet")
public class ChatSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 기본 세팅
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htmlk charset=UTF-8");
		// 사용자에게 입력받은 값 변수에 저장
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		// [보내는사람 or 받는 사람 아이디 or 채팅내용] 값이 비어있거나 ""(공백)인 경우 -> 오류 발생시키도록
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
				|| chatContent == null || chatContent.equals("")) {
			// 클라이언트에게 "0"이라는 문자 하나 반환
			response.getWriter().write("0");
		} else {
			// 디코딩하기
			fromID = URLDecoder.decode(fromID, "UTF-8");
			toID = URLDecoder.decode(toID, "UTF-8");
			chatContent = URLDecoder.decode(chatContent, "UTF-8");
			// 사용자(클라이언트)에게 값 반환하기 (DAO모델의 submit함수 호출한 반환값!!) -> 성공시 1
			// submit 메소드 : 다른사람에게 채팅 보내는 메서드
			// 메소드 작동 하면 -> 데이터베이스의 하나의 레코드가 등록 됨!
			response.getWriter().write(new ChatDAO().submit(fromID, toID, chatContent) + "");
		}
	}

}
