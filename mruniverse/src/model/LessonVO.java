package model;

public class LessonVO {
	private int no; // 일련번호
	private String l_num; // 과목번호
	private String l_name; // 과목명

	// 디폴트 생성자
	public LessonVO() {
		super();
	}

	// 일련번호를 제외한 생성자
	public LessonVO(String l_num, String l_name) {
		super();
		this.l_num = l_num;
		this.l_name = l_name;
	}

	// 전체필드 생성자
	public LessonVO(int no, String l_num, String l_name) {
		super();
		this.no = no;
		this.l_num = l_num;
		this.l_name = l_name;
	}

	// get,set
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getL_num() {
		return l_num;
	}

	public void setL_num(String l_num) {
		this.l_num = l_num;
	}

	public String getL_name() {
		return l_name;
	}

	public void setL_name(String l_name) {
		this.l_name = l_name;
	}

}
