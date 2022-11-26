package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserRegisterServlet
 */
@WebServlet("/UserRegisterServlet")
// 회원가입 담당 서블릿
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		// 사용자에게 입력받은 값 변수에 저장
		String userID = request.getParameter("userID");
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userName = request.getParameter("userName");
		String userAge = request.getParameter("userAge");
		String userGender = request.getParameter("userGender");
		String userEmail = request.getParameter("userEmail");
		String userProfile = request.getParameter("userProfile");
		// 해당 변수중 하나라도 null이거나 공백인 경우
//		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("")
//				|| userPassword2 == null || userPassword2.equals("") || userName == null || userName.equals("")
//				|| userAge == null || userAge.equals("") || userGender == null || userGender.equals("")
//				|| userEmail == null || userEmail.equals("")) {
//			// messgaeType과 content에 해당 내용이 session값으로 설정됨
//			request.getSession().setAttribute("messageType", "오류 메세지");
//			request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요.");
//			response.sendRedirect("join.jsp"); // 사용자를 해당 페이지로 강제 이동
//			return; // doPost 함수 종료
//		}
//		// 비밀번호와 비밀번호확인이 일치하지 않는 경우
//		if(!userPassword1.equals(userPassword2)) {
//			request.getSession().setAttribute("messageType", "오류 메세지");
//			request.getSession().setAttribute("messageContent", "비밀번호가 서로 다릅니다.");
//			response.sendRedirect("join.jsp"); // 사용자를 해당 페이지로 강제 이동
//			return; // doPost 함수 종료
//		}
		// 오류가 없는 경우!(위의 if문 2개에 걸리지 않은 경우)
		// 아이디 중복 검사
		int result = new UserDAO().register(userID, userPassword1, userName, userAge, userGender, userEmail, userProfile);
		if(result == 1) { // 중복된 아이디값이 없는 경우  -> 회원가입 success!!
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "회원가입에 성공했습니다.");
			response.sendRedirect("index.jsp"); // 해당 페이지로 강제 이동
			return; // doPost 함수 종료
		}
		else { // 중복된 아이디값이 있는 경우
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "이미 존재하는 회원입니다.");
			response.sendRedirect("join.jsp"); // 해당 페이지로 강제 이동
			return; // doPost 함수 종료
		}
	}

}
