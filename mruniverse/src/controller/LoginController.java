package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private Label lblLogin; // 로그인 라벨
	@FXML
	private TextField txtId; // 입력받을 아이디
	@FXML
	private PasswordField txtPassword; // 입력받을 비밀번호
	@FXML
	private Button btnCancel; // 취소버튼
	@FXML
	private Button btnLogin; // 로그인 버튼
	@FXML
	private Button btnJoin; // 등록버튼
	@FXML
	private ToggleGroup loginGroup; // 로그인 그룹
	@FXML
	private RadioButton rbManager; // 관리자표시
	@FXML
	private RadioButton rbStudent; // 학생표시
	@FXML
	private Label lblIconImg; // 등록한 이미지 라벨
	@FXML
	private ImageView iconImg;
	// 관리자명 정적변수?
	public static String managerName = "";
	// 학생아이디 정적변수?
	public static String studentId = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// 아이디 입력에서 엔터키 이벤트 적용
		txtId.setOnKeyPressed(event -> handerTxtIdKeyPressed(event));
		// 패스워드 입력에서 엔터키 이벤트 적용
		txtPassword.setOnKeyPressed(event -> handerTxtPasswordKeyPressed(event));
		// 관리자 등록창으로 전환 이벤트
		btnJoin.setOnAction(event -> handerBtnJoinAction(event));
		// 로그인버튼 이벤트
		btnLogin.setOnAction(event -> handlerBtnLoginAction(event));
		// 로그인창 닫기 이벤트
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event));
		// 관리자 라디오 버튼 선택시 이벤트
		rbManager.setOnAction(event -> handlerRbManagerAction(event));
		// 학생 라디오 버튼 선택시 이벤트
		rbStudent.setOnAction(event -> handlerRbStudentAction(event));
	}

	// 로그인버튼 이벤트
	public void handlerBtnLoginAction(ActionEvent event) {
		login(); // 로그인 메소드 호출
	}

	// 로그인 메소드
	public void login() {
		// LoginDAO 인스턴스화
		LoginDAO login = new LoginDAO();
		// StudentDAO 인스턴스화
		StudentDAO sLogin = new StudentDAO();
		// 로그인 성공여부 변수
		boolean sucess = false;

		try {
			// 선택된 토글버튼이 manager과 같으면
			if ("manager".equals(loginGroup.getSelectedToggle().getUserData().toString())) {
				managerName = managerLoginName();
				// LoginDAO에서 getLogin메소드에 아이디와 비밀번호를 공백제거후 넣어주고 성공여부를 반환받는다
				sucess = login.getLogin(txtId.getText().trim(), txtPassword.getText().trim());

				if (sucess) {
					// 로그인 성공여부가 true일 경우
					try {
						// 메인뷰를 불러온다
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
						// 부모창을 login.fxml로 로드
						Parent mView = (Parent) loader.load();
						System.out.println(mView);
						// Scene 객체 생성
						Scene scane = new Scene(mView);
						// Stage 객체 생성
						Stage mainMtage = new Stage();
						// 타이틀 설정
						mainMtage.setTitle("미래 대학교 학사관리");
						// 사이즈 재설정 불가
						mainMtage.setResizable(false);
						// 씬설정
						mainMtage.setScene(scane);
						// 그전에 있던창 oldStage로 저장
						Stage oldStage = (Stage) btnLogin.getScene().getWindow();
						// 그전에 있던 창을 닫음
						oldStage.close();
						// 등록창 열기
						mainMtage.show();
					} catch (IOException e) {
						System.out.println("오류 : " + e);
					}
				} else {
					// 로그인 성공여부가 false일 경우
					// 경고창을 보여준다
					Alert alert;
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("로그인 실패");
					alert.setHeaderText("아이디와 비밀번호 불일치");
					alert.setContentText("수고링~");
					// 경고창 크기설정 불가
					alert.setResizable(false);
					// 경고창을 보여주고 기다린다
					alert.showAndWait();
					// 입력한 아이디와 비밀번호를 지워준다
					txtId.clear();
					txtPassword.clear();
				}
				// 선택된 토글버튼이 학생일경우
			} else if ("student".equals(loginGroup.getSelectedToggle().getUserData().toString())) {
				// StudentDAO에서 getLogin메소드 호출
				sucess = sLogin.getLogin(txtId.getText().trim(), txtPassword.getText().trim()); // 로그인성공시 메인 페이지로 이동
				if (sucess) {
					try {
						// 입력받은 아이디를 학생아이디 변수에 넣어준다.
						studentId = txtId.getText(); // 수강뷰를 불러온다
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/trainee.fxml")); // 부모창을
																											// login.fxml로
																											// 로드
						Parent mainView = (Parent) loader.load(); // Scene 객체 생성
						Scene scane = new Scene(mainView); // Stage 객체 생성
						Stage mainMtage = new Stage(); // 타이틀 설정
						mainMtage.setTitle("미래 대학교 학사관리"); // 사이즈 재설정 불가
						mainMtage.setResizable(false); // 씬설정
						mainMtage.setScene(scane); // 그전에 있던창oldStage로 저장
						Stage oldStage = (Stage) btnLogin.getScene().getWindow(); // 그전에있던 창을 닫음
						oldStage.close(); // 등록창 열기
						mainMtage.show();
					} catch (IOException e) {
						System.out.println("오류 " + e);
					}
				}

				else { // 로그인 실패시
						// 경고창

					Alert alert;
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("로그인 실패");
					alert.setHeaderText("아이디와 비밀번호 불일치");
					alert.setContentText("수고링~"); // 경고창크기설정 불가
					alert.setResizable(false); // 경고창을 보여주고 기다린다
					alert.showAndWait();
					// 입력한 아이디와 비밀번호를 지워준다
					txtId.clear();
					txtPassword.clear();
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 입력받은 아이디나 비밀번호가 ""와 같을 경우
		if (txtId.getText().equals("") || txtPassword.getText().equals("")) {
			// 경고창
			Alert alert;
			alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인 실패");
			alert.setHeaderText("아이디나 비밀번호 미입력");
			alert.setContentText("수고링~");
			// 경고창 크기설정 불가
			alert.setResizable(false);
			// 경고창을 보여주고 기다린다
			alert.showAndWait();
			// 입력한 아이디와 비밀번호를 지워준다
			txtId.clear();
			txtPassword.clear();
		}

	}

	// 아이디에 맞는 이름을 가져오는 메소드
	public String managerLoginName() {
		// LoginDAO인스턴스화
		LoginDAO ldao = new LoginDAO();
		// 이름 변수
		String name = null;
		try {
			// LoginDAO에서 LoginName메소드에 아이디를 넣고 그에 맞는 이름을 반환하여 변수에 넣어준다
			name = ldao.getLoginName(txtId.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 이름 반환
		return name;
	}

	// 로그인창 닫기 이벤트
	public void handlerBtnCancelAction(ActionEvent event) {
		Platform.exit();

	}

	// 관리자 등록창으로 전환 이벤트
	public void handerBtnJoinAction(ActionEvent event) {
		try {
			// 관리자 등록창을 불러온다.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/join.fxml"));
			// 부모창을 login.fxml로 로드
			Parent mainView = (Parent) loader.load();
			// Scene 객체 생성
			Scene scane = new Scene(mainView);
			// Stage 객체 생성
			Stage mainMtage = new Stage();
			// 타이틀 설정
			mainMtage.setTitle("관리자 등록");
			// 씬설정
			mainMtage.setScene(scane);
			// 그전에 있던창 oldStage로 저장
			Stage oldStage = (Stage) btnLogin.getScene().getWindow();
			// 그전에 있던 창을 닫음
			oldStage.close();
			// 등록창 열기
			mainMtage.show();
		} catch (IOException e) {
			System.out.println("오류" + e);
		}

	}

	// 패스워드 입력에서 엔터키 이벤트 적용
	public void handerTxtPasswordKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			// 로그인 메소드 호출
			login();
		}

	}

	// 아이디 입력에서 엔터키 이벤트 적용
	public void handerTxtIdKeyPressed(KeyEvent event) {
		// 엔터키가 발생할경우
		if (event.getCode() == KeyCode.ENTER) {
			// 비밀번호창으로 포커스를 준다.
			txtPassword.requestFocus();
		}
	}

	// 관리자 라디오 버튼 선택시 이벤트
	public void handlerRbManagerAction(ActionEvent event) {
		// 관리자 이미지를 가져온다
		URL srtImg = getClass().getResource("../image/manager.png");
		// 뭥미
		Image image = new Image(srtImg.toString());
		iconImg.setImage(image);
		// 로그인 라벨에 학생로그인을 넣는다
		lblLogin.setText("관리자 로그인");
		btnJoin.setDisable(false); // 등록버튼 활성화
		btnLogin.setText("관리자 로그인 로그인"); // 버튼에 "학생 로그인"을 넣는다
	}

	// 학생 라디오 버튼 선택시 이벤트
	public void handlerRbStudentAction(ActionEvent event) {
		// 학생이미지를 가져온다
		URL srtImg = getClass().getResource("../image/student.png");
		// 뭥미
		Image image = new Image(srtImg.toString());
		iconImg.setImage(image);
		// 로그인 라벨에 학생로그인을 넣는다
		lblLogin.setText("학생 로그인");
		btnJoin.setDisable(true); // 등록버튼 비활성화
		btnLogin.setText("학생 로그인"); // 버튼에 "학생 로그인"을 넣는다
	}

	// 학생 이름을 가져오는 메소드
	public String studentLoginName() {
		// StudentDAO 인스턴스화
		StudentDAO sdao = new StudentDAO();
		String name = null;

		try {
			name = sdao.getLoginName(txtId.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

}
