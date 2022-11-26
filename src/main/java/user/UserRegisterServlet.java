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
// ȸ������ ��� ����
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		// ����ڿ��� �Է¹��� �� ������ ����
		String userID = request.getParameter("userID");
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userName = request.getParameter("userName");
		String userAge = request.getParameter("userAge");
		String userGender = request.getParameter("userGender");
		String userEmail = request.getParameter("userEmail");
		String userProfile = request.getParameter("userProfile");
		// �ش� ������ �ϳ��� null�̰ų� ������ ���
//		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("")
//				|| userPassword2 == null || userPassword2.equals("") || userName == null || userName.equals("")
//				|| userAge == null || userAge.equals("") || userGender == null || userGender.equals("")
//				|| userEmail == null || userEmail.equals("")) {
//			// messgaeType�� content�� �ش� ������ session������ ������
//			request.getSession().setAttribute("messageType", "���� �޼���");
//			request.getSession().setAttribute("messageContent", "��� ������ �Է��ϼ���.");
//			response.sendRedirect("join.jsp"); // ����ڸ� �ش� �������� ���� �̵�
//			return; // doPost �Լ� ����
//		}
//		// ��й�ȣ�� ��й�ȣȮ���� ��ġ���� �ʴ� ���
//		if(!userPassword1.equals(userPassword2)) {
//			request.getSession().setAttribute("messageType", "���� �޼���");
//			request.getSession().setAttribute("messageContent", "��й�ȣ�� ���� �ٸ��ϴ�.");
//			response.sendRedirect("join.jsp"); // ����ڸ� �ش� �������� ���� �̵�
//			return; // doPost �Լ� ����
//		}
		// ������ ���� ���!(���� if�� 2���� �ɸ��� ���� ���)
		// ���̵� �ߺ� �˻�
		int result = new UserDAO().register(userID, userPassword1, userName, userAge, userGender, userEmail, userProfile);
		if(result == 1) { // �ߺ��� ���̵��� ���� ���  -> ȸ������ success!!
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "ȸ�����Կ� �����߽��ϴ�.");
			response.sendRedirect("index.jsp"); // �ش� �������� ���� �̵�
			return; // doPost �Լ� ����
		}
		else { // �ߺ��� ���̵��� �ִ� ���
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "�̹� �����ϴ� ȸ���Դϴ�.");
			response.sendRedirect("join.jsp"); // �ش� �������� ���� �̵�
			return; // doPost �Լ� ����
		}
	}

}
