package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	// 커넥션풀을 이용하기 위한
		DataSource dataSource;
		
		public UserDAO() {
			// 해당 객체가 만들어지자마자 데이터베이스에 접속할 수 있도록
			try {
				InitialContext initContext = new InitialContext();
				// 실질적으로 소스에 접근할 수 있도록 만들어 ㅈ줌
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				// dataSource 초기화, UserChat에 접근할 수 있도록
				dataSource = (DataSource) envContext.lookup("jdbc/UserChat"); // 프로젝트명
			} catch(Exception e) {
				// 오류처리
				e.printStackTrace();
			}
		}
		
		// 회원과 관련한 데이터베이스 처리용 함수들
		// 로그인
		public int login(String userID, String userPassword) {
			Connection conn = null;
			// PreparedStatement : sql 인젝션?같은 해킹공격 방어, 안정적으로 sql문 사용하게 해줌
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "select * from user where userID = ?";
			try { // 우리가 작업할 내용
				// getConnection : 데이터베이스 커넥션풀에 접근하도록 만들어줌
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID); // ? 에 들어갈 문장
				rs = pstmt.executeQuery(); // sql문을 실행한 결과 담아주기
				if(rs.next()) { //결과(사용자)가 존재할 때
					// 사용자가 입력한 비번과 실제 등록한 비번이 같은지 비교
					if(rs.getString("userPassword").equals(userPassword)) {
						return 1; // 로그인 성공
					}
					return 2; // 비밀번호 틀릴 때 -> 로그인 실패
				} else { // 해당 사용자가 존재하지 않을 때
					return 0; // 존재하지 않는다고 알려줌
				}
				
			} catch(Exception e) {
				// 오류가 발생한 경우 출력
				e.printStackTrace();
			} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // 데이터베이스 종료
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // 데이터베이스 오류가 발생한 경우 알려중
		}
		
		// 중복체크
		public int registerCheck(String userID) {
			Connection conn = null;
			// PreparedStatement : sql 인젝션?같은 해킹공격 방어, 안정적으로 sql문 사용하게 해줌
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "select * from user where userID = ?";
			try { // 우리가 작업할 내용
				// getConnection : 데이터베이스 커넥션풀에 접근하도록 만들어줌
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID); // ? 에 들어갈 문장
				rs = pstmt.executeQuery(); // sql문을 실행한 결과 담아주기
				// 이미 해당 아이디가 존재하거나 userID가 공백일 경우
				if(rs.next() || userID.equals("")) { 
					return 0; // 이미 존재하는 회원
				} else {
					return 1; // 가입가능한 회원 아이디
				}
				
			} catch(Exception e) {
				// 오류가 발생한 경우 출력
				e.printStackTrace();
			} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // 데이터베이스 종료
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // 데이터베이스 오류가 발생한 경우 알려중
		}
		
		// 회원가입을 시도하는 함수
		public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {
			Connection conn = null;
			// PreparedStatement : sql 인젝션?같은 해킹공격 방어, 안정적으로 sql문 사용하게 해줌
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "insert into user values(?, ?, ?, ?, ?, ?, ?)";
			try { // 우리가 작업할 내용
				// getConnection : 데이터베이스 커넥션풀에 접근하도록 만들어줌
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				// 해당되는 ? 에 들어갈 문장
				pstmt.setString(1, userID);
				pstmt.setString(2, userPassword);
				pstmt.setString(3, userName);
				pstmt.setInt(4, Integer.parseInt(userAge)); // 숫자!
				pstmt.setString(5, userGender);
				pstmt.setString(6, userEmail);
				pstmt.setString(7, userProfile);
				rs = pstmt.executeQuery(); // sql문을 실행한 결과 담아주기
				// 이미 해당 아이디가 존재하거나 userID가 공백일 경우
				if(rs.next() || userID.equals("")) { 
					return 0; // 이미 존재하는 회원
				} else {
					return 1; // 가입가능한 회원 아이디
				}
				
			} catch(Exception e) {
				// 오류가 발생한 경우 출력
				e.printStackTrace();
			} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close(); // 데이터베이스 종료
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			return -1; // 데이터베이스 오류가 발생한 경우 알려중
		}
}
