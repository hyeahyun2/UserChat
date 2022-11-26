package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/UserRegisterCheckServlet")
// ���̵� �ߺ�üũ ��� ��ȯ
public class UserRegisterCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// post������� Ŭ���̾�Ʈ���� �Ű������� �޾��� ��� ó�� ���
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		// ����ڿ��� ��ȯ�� ��
		// ���ڿ����·� ����ϱ� ���� �������� ����("")�� �߰��� 
		response.getWriter().write(new UserDAO().registerCheck(userID) + "");
	}

}
