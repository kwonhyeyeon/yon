package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.JoinVO;

public class JoinDAO {
	// 관리자 등록
	public boolean getManagerRegiste(JoinVO jvo) throws Exception {

		// insert sql문
		String sql = "insert into managerjoin values (?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		// 등록성공 판단변수
		boolean joinSucess = false;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			pstmt.setString(1, jvo.getId());
			pstmt.setString(2, jvo.getPassword());
			pstmt.setString(3, jvo.getName());

			int i = pstmt.executeUpdate();
			// insert문이 성공적으로 입력되면 1을 반환한다
			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("관리자 등록");
				alert.setHeaderText(jvo.getName() + " 관리자 등록 완료.");
				alert.setContentText("수고링~");
				alert.showAndWait();
				// 등록성공변수 true값
				joinSucess = true;
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("관리자 등록");
				alert.setHeaderText("관리자 등록 실패");
				alert.setContentText("수고링~");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e = [ " + e + " ]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + " ]");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		// 등록 성공판단변수값 반환
		return joinSucess;
	}

	// 아이디 중복 체크
	public boolean getIdOverlap(String idOverlap) throws Exception {
		// 아이디에 맞는 정보가 있는지 체크하는 sql문
		String sql = "select * from managerjoin where id=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 중복아이디 체크 판단 변수
		boolean idOverlapResult = false;

		try {
			// DB연동
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			// 입력받은 아이디를 가져와서 넣어주고 쿼리문을 날린다
			pstmt.setString(1, idOverlap);
			// 쿼리문을 날린후 결과를 담는다
			rs = pstmt.executeQuery();
			// 읽어올게 있으면 ID중복
			if (rs.next()) {
				idOverlapResult = true;
			}

		} catch (SQLException e) {
			System.out.println("e = [ " + e + " ]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + " ]");
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
		// 아이디중복판단변수 boolean값 반환
		return idOverlapResult;
	}
}
