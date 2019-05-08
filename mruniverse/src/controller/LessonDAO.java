package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.LessonVO;

public class LessonDAO {
	// 과목 목록
	public ArrayList<LessonVO> getLessonTotalList() throws Exception {
		// ArrayList배열 생성
		ArrayList<LessonVO> list = new ArrayList<>();

		// lesson테이블에 있는 모든 정보를 일련번호로 정렬해서 가져오는 sql문
		String sql = "select * from lesson order by no";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 선언
		LessonVO lVo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 쿼리문을 날리고 나오는 결과를 넣어줌
			rs = pstmt.executeQuery();
			// 띄어쓰기 단위로 읽어온다.
			while (rs.next()) {
				// 인스턴스 화
				lVo = new LessonVO();
				// LessonVO에 레코드값들을 가져와 넣어주고 객체배열로 만든다.
				lVo.setNo(rs.getInt("no"));
				lVo.setL_num(rs.getString("l_num"));
				lVo.setL_name(rs.getString("l_name"));
				// list배열에 추가시킨다.
				list.add(lVo);

			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception se) {
			System.out.println(se);
		} finally {
			try { // DB연동에 사용되었던 오브젝트 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		// list배열 반환
		return list;
	}

	// 과목 등록
	public void getLessonRegiste(LessonVO lvo) throws Exception {
		// 과목을 등록하는 sql문
		String sql = "insert into lesson * values (lesson_seq.nextval, ?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 과목번호와 과목명은 입력받은걸로 sql문에 넣어준다.
			pstmt.setString(1, lvo.getL_num());
			pstmt.setString(2, lvo.getL_name());

			// insert문이 성공적으로 들어가면 1을 반환한다.
			int i = pstmt.executeUpdate();
			// 등록 성공
			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 등록");
				alert.setHeaderText(lvo.getL_name() + " 과목 등록 완료");
				alert.setContentText("수고링~");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 등록");
				alert.setHeaderText(" 과목 등록 실패");
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
	}

	// 데이터베이스에서 과목 테이블의 컬럼의 갯수
	public ArrayList<String> getLessonColumnName() throws Exception {

		// 컬럼명배열 생성
		ArrayList<String> columnName = new ArrayList<String>();

		// lesson테이블의 정보를 모두 가져오는 sql문
		String sql = "select * from lesson";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// ResultSetMetaData 객체 변수 선언
		// 컬럼명, 갯수를 가져오기 위한
		ResultSetMetaData rsmd = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// sql문을 날린후 결과 저장
			rs = pstmt.executeQuery();
			// 컬럼의 갯수
			int cols = rsmd.getColumnCount();
			// 컬럼의 갯수만큼 컬럼명 배열에 넣어준다.
			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception se) {
			System.out.println(se);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		// 컬럼명배열 반환
		return columnName;
	}

	// 과목 수정
	public boolean getLessonUpdate(int no, String l_num, String l_name) throws Exception {
		// 일련번호를 입력받아 과목번호와 과목명을 수정하는 sql문
		String sql = "update lesson set l_num=?, l_name=? where no=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 수정성공 판단변수
		boolean lessonUpdateSucess = false;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 수정된 과목번호와 과목명 입력
			pstmt.setString(1, l_num);
			pstmt.setString(2, l_name);
			// 수정할 행의 일련번호
			pstmt.setInt(3, no);

			// 수정이 성공하면 1을 반환
			int i = pstmt.executeUpdate();
			// 수정성공
			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 수정");
				alert.setHeaderText(l_name + " 과목 수정 완료");
				alert.setContentText("수고링~");
				alert.showAndWait();
				// 수정성공 판단변수 true값
				lessonUpdateSucess = true;
			} else { // 수정실패
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 수정");
				alert.setHeaderText(" 과목 수정 실패");
				alert.setContentText("수고링~");
				alert.showAndWait();
			}

		} catch (SQLException e) {
			System.out.println("e = [ " + e + " ]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + " ]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		} // 수정성공 판단여부 boolean값 반환
		return lessonUpdateSucess;
	}

	// 과목 삭제
	public boolean getLessonDelete(int no) throws Exception {
		// 바이트 단위로 입력되도록? 인스턴스화
		StringBuffer sql = new StringBuffer();
		// sql문에 일련번호로 삭제하는 delete문 저장
		sql.append("delete from lesson where no=?");

		Connection con = null;
		PreparedStatement pstmt = null;
		// 과목삭제 판단 변수
		boolean lessonDeleteSucess = false;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql.toString());
			// 입력받은 일련번호를 넣고 쿼리문을 날린다
			pstmt.setInt(1, no);
			// delete문을 성공적으로 날리면 1을 반환한다.
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 삭제");
				alert.setHeaderText(" 과목 삭제 완료");
				alert.setContentText("수고링~");
				alert.showAndWait();
				lessonDeleteSucess = true;
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 삭제");
				alert.setHeaderText(" 과목 삭제 실패");
				alert.setContentText("수고링~");
				alert.showAndWait();
			}

		} catch (SQLException e) {
			System.out.println("e = [ " + e + " ]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + " ]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		// 삭제성공여부 판단 변수 boolean값 반환
		return lessonDeleteSucess;
	}
}
