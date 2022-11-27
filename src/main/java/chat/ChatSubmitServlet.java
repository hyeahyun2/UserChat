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
		// �⺻ ����
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htmlk charset=UTF-8");
		// ����ڿ��� �Է¹��� �� ������ ����
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		// [�����»�� or �޴� ��� ���̵� or ä�ó���] ���� ����ְų� ""(����)�� ��� -> ���� �߻���Ű����
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
				|| chatContent == null || chatContent.equals("")) {
			// Ŭ���̾�Ʈ���� "0"�̶�� ���� �ϳ� ��ȯ
			response.getWriter().write("0");
		} else {
			// ���ڵ��ϱ�
			fromID = URLDecoder.decode(fromID, "UTF-8");
			toID = URLDecoder.decode(toID, "UTF-8");
			chatContent = URLDecoder.decode(chatContent, "UTF-8");
			// �����(Ŭ���̾�Ʈ)���� �� ��ȯ�ϱ� (DAO���� submit�Լ� ȣ���� ��ȯ��!!) -> ������ 1
			// submit �޼ҵ� : �ٸ�������� ä�� ������ �޼���
			// �޼ҵ� �۵� �ϸ� -> �����ͺ��̽��� �ϳ��� ���ڵ尡 ��� ��!
			response.getWriter().write(new ChatDAO().submit(fromID, toID, chatContent) + "");
		}
	}

}
