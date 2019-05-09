package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

	// 수강테이블의 컬럼명을 가져오는 메소드
	public ArrayList<String> getTraineeColumnName() throws Exception {

		// ArrayList배열 생성
		ArrayList<String> columnName = new ArrayList<String>();

		// 수강테이블의 모든 정보를 가져오는 sql문
		String sql = "select * from trainee";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// ResultSetMetaData 객체 변수 선언
		ResultSetMetaData rsmd = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 쿼리문을 날리고 결과를 담는다
			rs = pstmt.executeQuery();
			// 컬럼명을 가져와서 담아준다
			rsmd = rs.getMetaData();
			// 컬럼의 갯수를 담아주는 변수
			int cols = rsmd.getColumnCount();
			// rsmd의 컬럼명을 컬럼갯수만큼 가져와서 ArrayList배열 columnName에 넣어준다.
			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연결 해제
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

	// 수강 신청 삭제
	public boolean getTraineeDelete(int no) throws Exception {
		// 초기 문자열이 없고 16개의 문자를 저장할수 있는 버퍼를 가진 객체를 생성한다.
		StringBuffer sql = new StringBuffer();
		// 버퍼에 일련번호로 삭제하는 delete문 저장
		sql.append("delete from trainee where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		// 삭제 판단 변수
		boolean subjectDeleteSucess = false;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// 쿼리문을 담을 그릇
			pstmt = con.prepareStatement(sql.toString());
			// 쿼리문에 일련번호 입력
			pstmt.setInt(1, no);
			// delete문이 성공적으로 입력되면 1을 반환한다.
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 신청 취소");
				alert.setHeaderText("수강 신청 취소 완료");
				alert.setContentText("수고링~");
				alert.showAndWait();
				// 삭제 판단변수 true
				subjectDeleteSucess = true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 신청 취소");
				alert.setHeaderText("수강 신청 취소 실패");
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
		// 삭제 판단 변수 boolean값 반환
		return subjectDeleteSucess;
	}

	// 수강신청 전체 목록
	public ArrayList<TraineeVO> getTraineeTotalList() throws Exception {
		// ArrayList 배열 생성
		ArrayList<TraineeVO> list = new ArrayList<>();
		// 3개의 테이블에서 정보를 가져오는 SQL문
		String sql = "select tr.no as no, tr.sd_num as l_num, st.sd_name as sd_name, t_section, t_date "
				+ "from trainee tr, lesson le, student st "
				+ "where tr.l_num = le.l_num and tr.sd_num = st.sd_num order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 선언
		TraineeVO tVo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇..
			pstmt = con.prepareStatement(sql);
			// sql문을 날리고 결과를 저장한다.
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 인스턴스 생성
				tVo = new TraineeVO();
				// 쿼리문을 날리고 얻은 결과에서 값을 가져와 객체의 필드값을 설정한다.
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_scetion"));
				tVo.setT_date(rs.getString("t_date"));

				// 필드값을 설정해준후 ArrayList배열에 객체를 추가한다
				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연결 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
			}
		}
		// TraineeVO객체배열 반환
		return list;
	}

	// 전체 수강탭에서 학번 검색
	public ArrayList<TraineeVO> getTraineeStudentNumSearchList(String sd_num) throws Exception {
		// ArrayList배열 생성
		ArrayList<TraineeVO> list = new ArrayList<>();
		// 학번으로 3개의 테이블에서 데이터를 가져오는 sql문
		String sql = "select tr.no as no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name, t_section, "
				+ "t_date from trainee tr, lesson le, student st where tr.l_num = le.l_num and tr.sd_num = "
				+ "st.sd_num and tr.sd_num = ? order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 생성
		TraineeVO tVo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 학번입력
			pstmt.setString(1, sd_num);
			// sql문을 날리고 결과를 저장
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 인스턴스 생성
				tVo = new TraineeVO();
				// 쿼리문을 날리고 얻은 결과에서 값을 가져와 객체의 필드값을 설정한다.
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_scetion"));
				tVo.setT_date(rs.getString("t_date"));

				// 필드값을 설정해준후 ArrayList배열에 객체를 추가한다
				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연결 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
			}
		}
		// TraineeVO객체배열 반환
		return list;
	}

	// 전체 수강 탭에서 과목 검색
	public ArrayList<TraineeVO> getTraineeSubjectSearchList(String l_name) throws Exception {
		// 뭥미
		String l_num = getLessonNum(l_name);
		// 배열 생성
		ArrayList<TraineeVO> list = new ArrayList<>();
		// 과목으로 3개의 테이블에서 데이터를 가져오는 SQL문
		String sql = "select tr.no as no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name, t_section, t_date "
				+ "from trainee tr, lesson le, student st "
				+ "where tr.l_num = le.l_num and tr.l_num = ? and tr.sd_num = st.sd_num " + "order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 생성
		TraineeVO tVo = null;
		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 과목입력
			pstmt.setString(1, l_num);
			// sql문을 날리고 결과를 저장
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 인스턴스 생성
				tVo = new TraineeVO();
				// 쿼리문을 날리고 얻은 결과에서 값을 가져와 객체의 필드값을 설정한다.
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_scetion"));
				tVo.setT_date(rs.getString("t_date"));

				// 필드값을 설정해준후 ArrayList배열에 객체를 추가한다
				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연결 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
			}
		}
		// TraineeVO객체배열 반환
		return list;
	}

	// 전체 수강 탭에서 학생이름 검색
	public ArrayList<TraineeVO> getTraineeStudentNameSearchList(String sd_name) throws Exception {
		// 배열생성
		ArrayList<TraineeVO> list = new ArrayList<>();
		// 학생이름으로 3개의 테이블에서 데이터를 가져오는 sql문
		String sql = "select tr.no as no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name, t_section, t_date "
				+ "from trainee tr, lesson le, student st "
				+ "where tr.l_num = le.l_num and tr.sd_num = st.sd_num and st.sd_name = ? " + "order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 인스턴스 생성
		TraineeVO tVo = null;
		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담을 그릇
			pstmt = con.prepareStatement(sql);
			// 과목입력
			pstmt.setString(1, sd_name);
			// sql문을 날리고 결과를 저장
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 인스턴스 생성
				tVo = new TraineeVO();
				// 쿼리문을 날리고 얻은 결과에서 값을 가져와 객체의 필드값을 설정한다.
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_scetion"));
				tVo.setT_date(rs.getString("t_date"));

				// 필드값을 설정해준후 ArrayList배열에 객체를 추가한다
				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// DB연결 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
			}
		}
		// TraineeVO객체배열 반환
		return list;
	}
}
