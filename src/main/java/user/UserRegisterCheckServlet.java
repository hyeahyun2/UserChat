package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/UserRegisterCheckServlet")
// 아이디 중복체크 결과 반환
public class UserRegisterCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// post방식으로 클라이언트한테 매개변수를 받았을 경우 처리 방식
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		// 사용자에게 반환할 값
		// 문자열형태로 출력하기 위해 마지막에 공백("")을 추가함 
		response.getWriter().write(new UserDAO().registerCheck(userID) + "");
	}

}
