package model;

public class StudentVO {

	int no; // 일련번호
	String sd_num; // 학번
	String sd_name; // 이름
	String sd_id; // 아이디
	String sd_passwd; // 비밀번호
	String s_num; // 학과번호
	String sd_birthday; // 생년월일
	String sd_phone; // 핸드폰번호
	String sd_address; // 주소
	String sd_email; // 이메일
	String sd_date; // 등록일자
	
	public StudentVO() {
		super();
	}

	public StudentVO(int no, String sd_num, String sd_name, String sd_id, String sd_passwd, String s_num,
			String sd_birthday, String sd_phone, String sd_address, String sd_email, String sd_date) {
		super();
		this.no = no;
		this.sd_num = sd_num;
		this.sd_name = sd_name;
		this.sd_id = sd_id;
		this.sd_passwd = sd_passwd;
		this.s_num = s_num;
		this.sd_birthday = sd_birthday;
		this.sd_phone = sd_phone;
		this.sd_address = sd_address;
		this.sd_email = sd_email;
		this.sd_date = sd_date;
	}

	// 일련번호 삭제 생성자
	public StudentVO(String sd_num, String sd_name, String sd_id, String sd_passwd, String s_num, String sd_birthday,
			String sd_phone, String sd_address, String sd_email, String sd_date) {
		super();
		this.sd_num = sd_num;
		this.sd_name = sd_name;
		this.sd_id = sd_id;
		this.sd_passwd = sd_passwd;
		this.s_num = s_num;
		this.sd_birthday = sd_birthday;
		this.sd_phone = sd_phone;
		this.sd_address = sd_address;
		this.sd_email = sd_email;
		this.sd_date = sd_date;
	}

	// 일련번호, 등록날짜 뺀 생성자
	public StudentVO(String sd_num, String sd_name, String sd_id, String sd_passwd, String s_num, String sd_birthday,
			String sd_phone, String sd_address, String sd_email) {
		super();
		this.sd_num = sd_num;
		this.sd_name = sd_name;
		this.sd_id = sd_id;
		this.sd_passwd = sd_passwd;
		this.s_num = s_num;
		this.sd_birthday = sd_birthday;
		this.sd_phone = sd_phone;
		this.sd_address = sd_address;
		this.sd_email = sd_email;
	}

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

	public String getSd_id() {
		return sd_id;
	}

	public void setSd_id(String sd_id) {
		this.sd_id = sd_id;
	}

	public String getSd_passwd() {
		return sd_passwd;
	}

	public void setSd_passwd(String sd_passwd) {
		this.sd_passwd = sd_passwd;
	}

	public String getS_num() {
		return s_num;
	}

	public void setS_num(String s_num) {
		this.s_num = s_num;
	}

	public String getSd_birthday() {
		return sd_birthday;
	}

	public void setSd_birthday(String sd_birthday) {
		this.sd_birthday = sd_birthday;
	}

	public String getSd_phone() {
		return sd_phone;
	}

	public void setSd_phone(String sd_phone) {
		this.sd_phone = sd_phone;
	}

	public String getSd_address() {
		return sd_address;
	}

	public void setSd_address(String sd_address) {
		this.sd_address = sd_address;
	}

	public String getSd_email() {
		return sd_email;
	}

	public void setSd_email(String sd_email) {
		this.sd_email = sd_email;
	}

	public String getSd_date() {
		return sd_date;
	}

	public void setSd_date(String sd_date) {
		this.sd_date = sd_date;
	}

	@Override
	public String toString() {
		return "StudentVO [no=" + no + ", sd_num=" + sd_num + ", sd_name=" + sd_name + ", sd_id=" + sd_id
				+ ", sd_passwd=" + sd_passwd + ", s_num=" + s_num + ", sd_birthday=" + sd_birthday + ", sd_phone="
				+ sd_phone + ", sd_address=" + sd_address + ", sd_email=" + sd_email + ", sd_date=" + sd_date + "]";
	}
	
}
