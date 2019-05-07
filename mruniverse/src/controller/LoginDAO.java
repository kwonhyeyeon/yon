package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

	// 관리자 로그인(아이디와 패스워드를 가져와 정보가 있는지 boolean값 반환)
	public boolean getLogin(String loginId, String loginPassword) throws Exception {
		// id와 password가 있는지 확인하는 sql문
		String sql = "select * from managerjoin where id = ? and password = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 로그인 결과상태 변수
		boolean loginResult = false;

		try {
			// DB연결
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// id에 입력받은 loginId를 넣는다
			pstmt.setString(1, loginId);
			// password에 입력받은 loginPassword를 넣는다
			pstmt.setString(2, loginPassword);
			// sql문을 날리고 결과를 저장한다
			rs = pstmt.executeQuery();

			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			if (rs.next()) {
				loginResult = true;
			}

		} catch (SQLException e) {
			System.out.println("e = [ " + e + "]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + "]");
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		// 일치하는 값이 없으면 초기값 그대로 false반환
		return loginResult;
	}

	// 로그인시 입력받은 id와 일치하는 이름을 가져오는 메소드
	// 일치하는 id가 있으면 이름반환, 없으면 null값 반환
	public String getLoginName(String loginId) throws Exception {
		String sql = "select name from managerjoin where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String loginName = null;

		try {
			// DB연결
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// id에 입력받은 loginId를 넣는다
			pstmt.setString(1, loginId);
			// sql문을 날리고 결과를 저장한다
			rs = pstmt.executeQuery();
			// 결과로 가져온 이름의 첫번째를 변수에 저장한다
			if (rs.next()) {
				loginName = rs.getString(1);

			}
		} catch (SQLException e) {
			System.out.println("e = [ " + e + "]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + "]");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return loginName;
	}

}
