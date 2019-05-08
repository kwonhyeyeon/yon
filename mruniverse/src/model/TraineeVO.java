package model;

public class TraineeVO {
	int no; // 일련번호
	String sd_num; // 학생번호
	String sd_name; // 학생이름
	String l_num; // 과목번호
	String t_section; // 과목구분
	String t_date; // 등록일자
	
	// 디폴트 생성자
	public TraineeVO() {
		super();
	}

	// 학생번호, 과목번호, 과목구분 생성자
	public TraineeVO(String sd_num, String l_num, String t_section) {
		super();
		this.sd_num = sd_num;
		this.l_num = l_num;
		this.t_section = t_section;
	}

	// 학생이름을 제외한 생성자
	public TraineeVO(int no, String sd_num, String l_num, String t_section, String t_date) {
		super();
		this.no = no;
		this.sd_num = sd_num;
		this.l_num = l_num;
		this.t_section = t_section;
		this.t_date = t_date;
	}

	// 일련번호를 제외한 생성자
	public TraineeVO(String sd_num, String sd_name, String l_num, String t_section, String t_date) {
		super();
		this.sd_num = sd_num;
		this.sd_name = sd_name;
		this.l_num = l_num;
		this.t_section = t_section;
		this.t_date = t_date;
	}

	// get,set
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSd_num() {
		return sd_num;
	}

	public void setSd_num(String sd_num) {
		this.sd_num = sd_num;
	}

	public String getSd_name() {
		return sd_name;
	}

	public void setSd_name(String sd_name) {
		this.sd_name = sd_name;
	}

	public String getL_num() {
		return l_num;
	}

	public void setL_num(String l_num) {
		this.l_num = l_num;
	}

	public String getT_section() {
		return t_section;
	}

	public void setT_section(String t_section) {
		this.t_section = t_section;
	}

	public String getT_date() {
		return t_date;
	}

	public void setT_date(String t_date) {
		this.t_date = t_date;
	}

}
