package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.StudentVO;

public class TraineeDAO {
	// 로그인한 학생의 정보
	public StudentVO getStudentSubjectName(String sd_id) throws Exception{
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
			
			if(rs.next()) {
				// 인스턴스 생성
				studentInfo = new StudentVO();
				studentInfo.setSd_num(rs.getString("sd_num"));
				studentInfo
				
			}
			
			
		}catch(SQLException se) {
			System.out.println(se);
		}catch(Exception se) {
			System.out.println(se);
		}
		
		
		
		
		
		
	}
}
