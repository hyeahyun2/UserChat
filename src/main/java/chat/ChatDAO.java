package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ChatDAO {
	// Ŀ�ؼ�Ǯ�� �̿��ϱ� ����
	// ��, �����ͺ��̽��� �����ϱ� ���� �޼ҵ�!
	DataSource dataSource;
	
	public ChatDAO() {
		// �ش� ��ü�� ��������ڸ��� �����ͺ��̽��� ������ �� �ֵ���
		try {
			InitialContext initContext = new InitialContext();
			// ���������� �ҽ��� ������ �� �ֵ��� ����� ����
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			// dataSource �ʱ�ȭ, UserChat�� ������ �� �ֵ���
			dataSource = (DataSource) envContext.lookup("jdbc/UserChat"); // ������Ʈ��
		} catch(Exception e) {
			// ����ó��
			e.printStackTrace();
		}
	}
	
	// Ư�� ���̵� ���� ä�� ������ ��������
	public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID){
		// �ϳ��ϳ��� �޼����� ����Ʈ�� ��Ƽ� �����ϱ� ���� ����Ʈ ��ü ����(����)�ϱ�
		ArrayList<ChatDTO> chatList = null;;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// ���̵� �޴»��or�ִ»���� �ش��ϰ� �Ǹ� select�ϱ�(�ð��� ����, chatID�� ���������� �ҷ��� chatID���� Ŭ ��!)
		String SQL = "select * from chat where ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > ? ORDER BY chatTime";
		try {
			conn = dataSource.getConnection(); // db ����
			pstmt = conn.prepareStatement(SQL); // sql�� ���
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID)); // int���´� ������ Integer.parseInt�� ����ȯ�ؼ� ��������!!
			rs = pstmt.executeQuery(); // sql������ ������ ��� ��������
			// sql������ ������ �� ����(try�� �� ����, �� ���ο� ä���� �Է��� �� ����) ���ο� ����Ʈ �ε����� ��
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				// ChatDTO �ڷ������� ���� => �޼��� ���� ������ ���� => �������� chatList�� �ϳ��� ���(�ε���)�� �ֱ�
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				// Ư�����ڴ� html�� ���Ǵ� Ű����� �ٲ��ֱ�
				chat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				// chatTime �����ͼ� ���� ���� ������ => chatTime�� ���
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "����";
				if(chatTime > 12) {
					timeType ="����";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " : " + rs.getString("chatTime").substring(14, 16) + "");
				chatList.add(chat); // chat�� ��� chatList�� �ϳ��� ��ҷ� �߰�
			}
		} catch (Exception e) { // ����ó��
			e.printStackTrace();
		} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return chatList; // ä�� ����Ʈ ��ȯ
	}
	
	// ��ȭ ���� �� �ֱ� �� ��� �̾Ƽ� ��������
	// �Ű������� int number �߰�
	public ArrayList<ChatDTO> getChatListByRecent(String fromID, String toID, int number){
		// �ϳ��ϳ��� �޼����� ����Ʈ�� ��Ƽ� �����ϱ� ���� ����Ʈ ��ü ����(����)�ϱ�
		ArrayList<ChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// ���̵� �޴»��or�ִ»���� �ش��ϰ� �Ǹ� select�ϱ�(�ð��� ����!)
		// (select MAX(chatID) - ? from chat) : ���� chat���� ���� chatID�� ū ������ number ��ŭ ���� ��
		String SQL = "select * from chat where ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > (select MAX(chatID) - ? from chat) ORDER BY chatTime";
		try {
			conn = dataSource.getConnection(); // db ����
			pstmt = conn.prepareStatement(SQL); // sql�� ���
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number); // 
			rs = pstmt.executeQuery(); // sql������ ������ ��� ��������
			// sql������ ������ �� ����(try�� �� ����, �� ���ο� ä���� �Է��� �� ����) ���ο� ����Ʈ �ε����� ��
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				// ChatDTO �ڷ������� ���� => �޼��� ���� ������ ���� => �������� chatList�� �ϳ��� ���(�ε���)�� �ֱ�
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				// Ư�����ڴ� html�� ���Ǵ� Ű����� �ٲ��ֱ�
				chat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				// chatTime �����ͼ� ���� ���� ������ => chatTime�� ���
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "����";
				if(chatTime > 12) {
					timeType ="����";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " : " + rs.getString("chatTime").substring(14, 16) + "");
				chatList.add(chat); // chat�� ��� chatList�� �ϳ��� ��ҷ� �߰�
			}
		} catch (Exception e) { // ����ó��
			e.printStackTrace();
		} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return chatList; // ä�� ����Ʈ ��ȯ
	}
	
	// �ٸ�������� ä�� ������ �޼���
	// ���ۿ��θ� �Ǵ��ϱ� ���� int�� ��ȯ (��(1)/����(������ ��) ��ȯ)
	public int submit(String fromID, String toID, String chatContent){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// ���̵� �޴»��or�ִ»���� �ش��ϰ� �Ǹ� select�ϱ�(�ð��� ����!)
		// (select MAX(chatID) - ? from chat) : ���� chat���� ���� chatID�� ū ������ ? ��ŭ ���� ��
		// (chatID, �������, �޴»��, ê����, ����ð�) .... NOW() : ���� �ð��� ��ȯ���ִ� Ű����
		String SQL = "insert into chat values (Null, ?, ?, ?, NOW())";
		try {
			conn = dataSource.getConnection(); // db ����
			pstmt = conn.prepareStatement(SQL); // sql�� ���
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
			return pstmt.executeUpdate(); // sql�� ���� ��� ��ȯ (������ -> 1, ���� -> 0 ��ȯ)
		} catch (Exception e) { // ����ó��
			e.printStackTrace();
		} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // ��ȯ�� �� ���� �� ������ -> �����ͺ��̽� ������ �߻��������� return��
	}
}
