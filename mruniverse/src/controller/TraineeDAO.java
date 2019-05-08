package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.StudentVO;
import model.TraineeVO;

public class TraineeDAO {
	// 로그인한 학생의 정보
	public StudentVO getStudentSubjectName(String sd_id) throws Exception {
		// sql문
		String sql = "select sd_num, sd_name, (select s_name from subject where s_num = (select s_num from student where sd_id = ?)) as s_num from student where sd_id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 객체 인스턴스 선언
		StudentVO studentInfo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 아이디를 입력받고 sql문에 넣어준다
			pstmt.setString(1, sd_id);
			pstmt.setString(2, sd_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				// 인스턴스 생성
				studentInfo = new StudentVO();
				// 객체에 쿼리문을 날리고 나온 결과 (sd_num, sd_name, s_num)을 넣어준다.
				studentInfo.setSd_num(rs.getString("sd_num"));
				studentInfo.setSd_name(rs.getString("sd_name"));
				studentInfo.setS_num(rs.getString("s_num"));
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
			} catch (SQLException e) {

			}
		}
		// 쿼리문 결과값을 넣어준 객체 반환
		return studentInfo;
	}

	// 선택한 과목명의 과목 번호
	public String getLessonNum(String lessonName) throws Exception {
		// 과목번호 변수
		String l_num = "";
		// 과목명으로 과목번호를 찾는 sql문
		String sql = "select l_num from lesson where l_name = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 과목명을 넣고 sql문을 담는다
			pstmt.setString(1, lessonName);
			// 쿼리문을 날린후 결과를 담는다
			rs = pstmt.executeQuery();
			// 결과를 띄어쓰기 단위로 읽어와 변수에 넣는다
			if (rs.next()) {
				l_num = rs.getString("l_num");
			} else {
				// 읽어올게 없을경우
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 과목의 과목 번호");
				alert.setHeaderText("선택한 " + lessonName + "과목의 과목번호가 없습니다");
				alert.setContentText("수고링~");
				alert.showAndWait();
			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연동에 사용되었던 오브젝트 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		// 과목번호 반환
		return l_num;
	}

	// 수강신청
	public void getTraineeRegiste(TraineeVO tvo) throws Exception {
		// sql문 (sd_num, l_num은 외래키임 부모테이블에 값이 있어야지만 insert를 할수 있다.)
		String sql = "insert into trainee values (trainee_seq.nextval, ?, ?, ?, sysdate)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 무결성 제약조건? 에 의해 부모키에 값이 있어야지만 insert할수 있다.
			// 때문에 부모키에 값이 있는것만 넣을수 있게 값을 가져와서 넣어준다.
			pstmt.setString(1, tvo.getSd_num());
			pstmt.setString(2, tvo.getL_num());
			pstmt.setString(3, tvo.getT_section());

			// insert문 성공시 1반환
			int i = pstmt.executeUpdate();

			// insert성공시
			if (i == 1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 신청");
				alert.setHeaderText("수강 신청 완료");
				alert.setContentText("수고링~");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 신청");
				alert.setHeaderText("수강 신청 실패");
				alert.setContentText("수고링~");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e = [ " + e + " ]");
		} catch (Exception e) {
			System.out.println("e = [ " + e + " ]");
		} finally {
			try {
				// DB연결 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

	}

	// 로그인한 학생 수강 신청 전체 목록(학번으로 검색)
	public ArrayList<TraineeVO> getTraineeTotalList(String sd_num) throws Exception {
		// ArrayList배열 생성(TraineeVO 타입)
		ArrayList<TraineeVO> list = new ArrayList<>();

		// 학생번호로 검색하여 일련번호, 학생번호, 과목명, 과목분류, 등록일을 가져오는 sql문
		String sql = "select tr.no as no, sd_num, le.l_name as l_num, t_section, t_date "
				+ "from trainee tr, lesson le where tr.l_num = le.l_num and " + "tr.sd_num = ? order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 변수 선언
		TraineeVO tVo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 검색할 학생번호
			pstmt.setString(1, sd_num);
			// sql문을 날리고 결과를 담는다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 인스턴스 생성
				tVo = new TraineeVO();
				// 일련번호를 가져와서 tVo객체에 넣어준다
				tVo.setNo(rs.getInt("no"));
				// 학생번호를 가져와서 tVo객체에 넣어준다
				tVo.setSd_num(rs.getString("sd_num"));
				// 과목번호를 가져와서 tVo객체에 넣어준다
				tVo.setL_num(rs.getString("l_num"));
				// 과목분류를 가져와서 tVo객체에 넣어준다
				tVo.setT_section(rs.getString("t_section"));
				// 등록일자를 가져와서 tVo객체에 넣어준다
				tVo.setT_date(rs.getString("t_date"));

				list.add(tVo);

			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연동 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return list; 
	}
}
