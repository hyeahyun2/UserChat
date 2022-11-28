package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// �⺻ ����
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htmlk charset=UTF-8");
		// 
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String listType = request.getParameter("listType");
		// [�����»�� or �޴� ��� ���̵� or ä�ó���] ���� ����ְų� ""(����)�� ��� -> ���� �߻���Ű����
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
				|| listType == null || listType.equals("")) {
			// Ŭ���̾�Ʈ���� ���� ���ڿ��� ��ȯ
			response.getWriter().write("");
		} 
		else if (listType.equals("ten")) {// �ѱ۷� �ۼ����� �� ��� -> URLDecoder ���
			response.getWriter().write(getTen(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8")));
		}
		else {
			try {// Ư���� ä�� ���̵��� �������� ��ȭ ������ ��������
				response.getWriter().write(getID(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8"), listType));
			} catch(Exception e) { // ����ó�� -> �����߻��� ���� ���ڿ� ��ȯ 
				response.getWriter().write("");
			}
		}
	}
	
	// getTen �޼ҵ�, ������ ó�� �ε���� �� �� �� ���� (���� �ֱ� ä�� ���� 10�� ������)
	public String getTen(String fromID, String toID) {
		// StringBuffer �ν��Ͻ� ���� (""(���鹮��)�� �����ϵ���)
		StringBuffer result = new StringBuffer("");
		// result �� �߰��ϱ� (�޺κп�)
		result.append("{\"result\":["); // json ���� !!!
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 10);
		if(chatList.size() == 0) return ""; // chatList�� ��� �ִ� ��� -> ���鹮�� ��ȯ
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			// ������ ��Ұ� �ƴ϶�� -> ���� ��Ҹ� �ձ� ���� �޸�(,) �߰�
			if(i != chatList.size() -1) result.append(","); 
		}
		// value��� ���� �ݾ��ֱ� + ���� �������� �ش��ϴ� ä���� chatID�� �����ͼ� ��� -> json ���� ������!(�ݾ��ֱ�)
		result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID() + "\"}");
		return result.toString(); // => json������ ���ڿ� ��ȯ
	}
	
	// getID �޼ҵ�
	public String getID(String fromID, String toID, String chatID) {
		// StringBuffer �ν��Ͻ� ���� (""(���鹮��)�� �����ϵ���)
		StringBuffer result = new StringBuffer("");
		// result �� �߰��ϱ� (�޺κп�)
		result.append("{\"result\":["); // json ���� !!!
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		if(chatList.size() == 0) return ""; // chatList�� ��� �ִ� ��� -> ���鹮�� ��ȯ
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			// ������ ��Ұ� �ƴ϶�� -> ���� ��Ҹ� �ձ� ���� �޸�(,) �߰�
			if(i != chatList.size() -1) result.append(",");
		}
		// value��� ���� �ݾ��ֱ� + ���� �������� �ش��ϴ� ä���� chatID�� �����ͼ� ��� -> json ���� ������!(�ݾ��ֱ�)
		result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID() + "\"}");
		return result.toString(); // => json������ ���ڿ� ��ȯ
	}

}
