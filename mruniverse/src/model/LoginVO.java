package model;

public class LoginVO {
	private String id;

	// 디폴트 생성자
	public LoginVO() {
		super();
	}

	public LoginVO(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
