package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	// Ŀ�ؼ�Ǯ�� �̿��ϱ� ����
		DataSource dataSource;
		
		public UserDAO() {
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
		
		// ȸ���� ������ �����ͺ��̽� ó���� �Լ���
		// �α���
		public int login(String userID, String userPassword) {
			Connection conn = null;
			// PreparedStatement : sql ������?���� ��ŷ���� ���, ���������� sql�� ����ϰ� ����
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "select * from user where userID = ?";
			try { // �츮�� �۾��� ����
				// getConnection : �����ͺ��̽� Ŀ�ؼ�Ǯ�� �����ϵ��� �������
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID); // ? �� �� ����
				rs = pstmt.executeQuery(); // sql���� ������ ��� ����ֱ�
				if(rs.next()) { //���(�����)�� ������ ��
					// ����ڰ� �Է��� ����� ���� ����� ����� ������ ��
					if(rs.getString("userPassword").equals(userPassword)) {
						return 1; // �α��� ����
					}
					return 2; // ��й�ȣ Ʋ�� �� -> �α��� ����
				} else { // �ش� ����ڰ� �������� ���� ��
					return 0; // �������� �ʴ´ٰ� �˷���
				}
				
			} catch(Exception e) {
				// ������ �߻��� ��� ���
				e.printStackTrace();
			} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // �����ͺ��̽� ����
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // �����ͺ��̽� ������ �߻��� ��� �˷���
		}
		
		// �ߺ�üũ
		public int registerCheck(String userID) {
			Connection conn = null;
			// PreparedStatement : sql ������?���� ��ŷ���� ���, ���������� sql�� ����ϰ� ����
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "select * from user where userID = ?";
			try { // �츮�� �۾��� ����
				// getConnection : �����ͺ��̽� Ŀ�ؼ�Ǯ�� �����ϵ��� �������
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID); // ? �� �� ����
				rs = pstmt.executeQuery(); // sql���� ������ ��� ����ֱ�
				// �̹� �ش� ���̵� �����ϰų� userID�� ������ ���
				if(rs.next() || userID.equals("")) { 
					return 0; // �̹� �����ϴ� ȸ��
				} else {
					return 1; // ���԰����� ȸ�� ���̵�
				}
				
			} catch(Exception e) {
				// ������ �߻��� ��� ���
				e.printStackTrace();
			} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // �����ͺ��̽� ����
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // �����ͺ��̽� ������ �߻��� ��� �˷���
		}
		
		// ȸ�������� �õ��ϴ� �Լ�
		public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {
			Connection conn = null;
			// PreparedStatement : sql ������?���� ��ŷ���� ���, ���������� sql�� ����ϰ� ����
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "insert into user values(?, ?, ?, ?, ?, ?, ?)";
			try { // �츮�� �۾��� ����
				// getConnection : �����ͺ��̽� Ŀ�ؼ�Ǯ�� �����ϵ��� �������
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				// �ش�Ǵ� ? �� �� ����
				pstmt.setString(1, userID);
				pstmt.setString(2, userPassword);
				pstmt.setString(3, userName);
				pstmt.setInt(4, Integer.parseInt(userAge)); // ����!
				pstmt.setString(5, userGender);
				pstmt.setString(6, userEmail);
				pstmt.setString(7, userProfile);
				rs = pstmt.executeQuery(); // sql���� ������ ��� ����ֱ�
				// �̹� �ش� ���̵� �����ϰų� userID�� ������ ���
				if(rs.next() || userID.equals("")) { 
					return 0; // �̹� �����ϴ� ȸ��
				} else {
					return 1; // ���԰����� ȸ�� ���̵�
				}
				
			} catch(Exception e) {
				// ������ �߻��� ��� ���
				e.printStackTrace();
			} finally { // sql������ ������ ���� �� -> ��� ���ҽ� �ݾ��ֵ���
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // �����ͺ��̽� ����
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // �����ͺ��̽� ������ �߻��� ��� �˷���
		}
}
