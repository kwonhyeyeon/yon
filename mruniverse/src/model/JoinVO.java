package model;

public class JoinVO {

	private String id;
	private String password;
	private String name;

	// 디폴트 생성자
	public JoinVO() {
		super();
	}

	// 아이디와 비밀번호만 있는 생성자
	public JoinVO(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	// 전체필드 생성자
	public JoinVO(String id, String password, String name) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
	}

	// get,set
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
