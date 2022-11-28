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
		// 기본 세팅
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/htmlk charset=UTF-8");
		// 
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String listType = request.getParameter("listType");
		// [보내는사람 or 받는 사람 아이디 or 채팅내용] 값이 비어있거나 ""(공백)인 경우 -> 오류 발생시키도록
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
				|| listType == null || listType.equals("")) {
			// 클라이언트에게 공백 문자열을 반환
			response.getWriter().write("");
		} 
		else if (listType.equals("ten")) {// 한글로 작성했을 때 대비 -> URLDecoder 사용
			response.getWriter().write(getTen(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8")));
		}
		else {
			try {// 특정한 채팅 아이디값을 기준으로 대화 정보를 가져오기
				response.getWriter().write(getID(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8"), listType));
			} catch(Exception e) { // 예외처리 -> 오류발생시 공백 문자열 반환 
				response.getWriter().write("");
			}
		}
	}
	
	// getTen 메소드, 문서가 처음 로드됐을 때 한 번 실행 (제일 최근 채팅 내역 10개 들고오기)
	public String getTen(String fromID, String toID) {
		// StringBuffer 인스턴스 생성 (""(공백문자)로 시작하도록)
		StringBuffer result = new StringBuffer("");
		// result 값 추가하기 (뒷부분에)
		result.append("{\"result\":["); // json 형태 !!!
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 10);
		if(chatList.size() == 0) return ""; // chatList가 비어 있는 경우 -> 공백문자 반환
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			// 마지막 요소가 아니라면 -> 다음 요소를 잇기 위해 콤마(,) 추가
			if(i != chatList.size() -1) result.append(","); 
		}
		// value담는 공간 닫아주기 + 가장 마지막에 해당하는 채팅의 chatID를 가져와서 담기 -> json 문장 끝내기!(닫아주기)
		result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID() + "\"}");
		return result.toString(); // => json형태의 문자열 반환
	}
	
	// getID 메소드
	public String getID(String fromID, String toID, String chatID) {
		// StringBuffer 인스턴스 생성 (""(공백문자)로 시작하도록)
		StringBuffer result = new StringBuffer("");
		// result 값 추가하기 (뒷부분에)
		result.append("{\"result\":["); // json 형태 !!!
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		if(chatList.size() == 0) return ""; // chatList가 비어 있는 경우 -> 공백문자 반환
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			// 마지막 요소가 아니라면 -> 다음 요소를 잇기 위해 콤마(,) 추가
			if(i != chatList.size() -1) result.append(",");
		}
		// value담는 공간 닫아주기 + 가장 마지막에 해당하는 채팅의 chatID를 가져와서 담기 -> json 문장 끝내기!(닫아주기)
		result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID() + "\"}");
		return result.toString(); // => json형태의 문자열 반환
	}

}
