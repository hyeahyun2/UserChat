package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ChatDAO {
	// 커넥션풀을 이용하기 위한
	// 즉, 데이터베이스에 접근하기 관련 메소드!
	DataSource dataSource;
	
	public ChatDAO() {
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
	
	// 특정 아이디에 따라 채팅 내역을 가져오기
	public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID){
		// 하나하나의 메세지를 리스트에 담아서 보관하기 위해 리스트 객체 생성(선언)하기
		ArrayList<ChatDTO> chatList = null;;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 아이디가 받는사람or주는사람에 해당하게 되면 select하기(시간순 정렬, chatID가 마지막으로 불러온 chatID보다 클 때!)
		String SQL = "select * from chat where ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > ? ORDER BY chatTime";
		try {
			conn = dataSource.getConnection(); // db 연결
			pstmt = conn.prepareStatement(SQL); // sql문 사용
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID)); // int형태는 무조껀 Integer.parseInt로 형변환해서 가져오기!!
			rs = pstmt.executeQuery(); // sql문장을 실행한 결과 가져오기
			// sql문장을 실행할 때 마다(try될 때 마다, 즉 새로운 채팅을 입력할 때 마다) 새로운 리스트 인덱스에 저
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				// ChatDTO 자료형으로 선언 => 메세지 관련 내역들 저장 => 마지막에 chatList의 하나의 요소(인덱스)로 넣기
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				// 특수문자는 html에 사용되는 키워드로 바꿔주기
				chat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				// chatTime 가져와서 오전 오후 나누기 => chatTime에 담기
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "오전";
				if(chatTime > 12) {
					timeType ="오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " : " + rs.getString("chatTime").substring(14, 16) + "");
				chatList.add(chat); // chat을 모두 chatList의 하나의 요소로 추가
			}
		} catch (Exception e) { // 예외처리
			e.printStackTrace();
		} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return chatList; // 채팅 리스트 반환
	}
	
	// 대화 내역 중 최근 것 몇개만 뽑아서 가져오기
	// 매개변수에 int number 추가
	public ArrayList<ChatDTO> getChatListByRecent(String fromID, String toID, int number){
		// 하나하나의 메세지를 리스트에 담아서 보관하기 위해 리스트 객체 생성(선언)하기
		ArrayList<ChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 아이디가 받는사람or주는사람에 해당하게 되면 select하기(시간순 정렬!)
		// (select MAX(chatID) - ? from chat) : 현재 chat에서 가장 chatID가 큰 값에서 number 만큼 빼준 것
		String SQL = "select * from chat where ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > (select MAX(chatID) - ? from chat) ORDER BY chatTime";
		try {
			conn = dataSource.getConnection(); // db 연결
			pstmt = conn.prepareStatement(SQL); // sql문 사용
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number); // 
			rs = pstmt.executeQuery(); // sql문장을 실행한 결과 가져오기
			// sql문장을 실행할 때 마다(try될 때 마다, 즉 새로운 채팅을 입력할 때 마다) 새로운 리스트 인덱스에 저
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				// ChatDTO 자료형으로 선언 => 메세지 관련 내역들 저장 => 마지막에 chatList의 하나의 요소(인덱스)로 넣기
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				// 특수문자는 html에 사용되는 키워드로 바꿔주기
				chat.setFromID(rs.getString("fromID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				// chatTime 가져와서 오전 오후 나누기 => chatTime에 담기
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "오전";
				if(chatTime > 12) {
					timeType ="오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " : " + rs.getString("chatTime").substring(14, 16) + "");
				chatList.add(chat); // chat을 모두 chatList의 하나의 요소로 추가
			}
		} catch (Exception e) { // 예외처리
			e.printStackTrace();
		} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return chatList; // 채팅 리스트 반환
	}
	
	// 다른사람에게 채팅 보내는 메서드
	// 전송여부를 판단하기 위해 int를 반환 (참(1)/거짓(나머지 수) 반환)
	public int submit(String fromID, String toID, String chatContent){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 아이디가 받는사람or주는사람에 해당하게 되면 select하기(시간순 정렬!)
		// (select MAX(chatID) - ? from chat) : 현재 chat에서 가장 chatID가 큰 값에서 ? 만큼 빼준 것
		// (chatID, 보낸사람, 받는사람, 챗내용, 현재시간) .... NOW() : 현재 시간을 반환해주는 키워드
		String SQL = "insert into chat values (Null, ?, ?, ?, NOW())";
		try {
			conn = dataSource.getConnection(); // db 연결
			pstmt = conn.prepareStatement(SQL); // sql문 사용
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
			return pstmt.executeUpdate(); // sql문 실행 결과 반환 (성공시 -> 1, 실패 -> 0 반환)
		} catch (Exception e) { // 예외처리
			e.printStackTrace();
		} finally { // sql문장이 실행이 끝난 뒤 -> 모든 리소스 닫아주도록
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 반환될 수 없는 값 적어줌 -> 데이터베이스 오류가 발생했을때의 return값
	}
}
