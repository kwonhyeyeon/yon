package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MainController implements Initializable {

	// 메뉴
	@FXML
	private MenuItem menuLogout; // 로그아웃
	@FXML
	private MenuItem menuExit; // 종료
	@FXML
	private MenuItem menuInfo; // 프로그램 정보

	// 탭
	@FXML
	private TabPane mainPane; // 메인탭
	@FXML
	private Tab subject; // 학과 등록 탭
	@FXML
	// 참조변수명 부여 방법 : include시 명시한 id + "Controller"
	private SubjectTabController subjectTabController;
	@FXML
	private Tab student; // 학생 등록 탭
	@FXML
	private StudentTabController studentTabController;
	@FXML
	private Tab lesson; // 과목 등록 탭
	@FXML
	private LessonTabController lessonTabController;
	@FXML
	private Tab traineeTotal; // 수강 신청 목록 탭
	@FXML
	private TraineeTotalTabController traineeTotalTabController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 탭 설정
		try {
			mainPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

				@Override
				public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

					if (newValue == subject) { // 학과 등록 탭일 경우
						System.out.println("학과");

						try {
							// 학과 등록 탭 컨트롤러의 subjectTotalList 메소드 호출
							subjectTabController.subjectTotalList();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (newValue == student) { // 학생 등록 탭일 경우
						try {
							// 학생 등록 탭 컨트롤러의 studentTotalList 메소드 호출
							studentTabController.studentTotalList();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (newValue == lesson) { // 과목 등록 탭일 경우
						try {
							// 과목 등록 탭 컨트롤러의 lessonTotalList 메소드 호출
							lessonTabController.lessonTotalList();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (newValue == traineeTotal) { // 수강 신청 목록 탭일 경우
						try {
							// 수강 신청 목록 탭 컨트롤러의 lessonTotalList 메소드 호출
							traineeTotalTabController.traineeTotalList();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

			});
			
			// 메뉴 이벤트 등록
			menuLogout.setOnAction(event -> handlerMenuLogoutAction(event));
			menuExit.setOnAction(event -> handlerMenuExitAction(event));
			menuInfo.setOnAction(event -> handlerMenuInfoAction(event));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handlerMenuInfoAction(ActionEvent event) {
		
		Alert alert;
		alert = new Alert(AlertType.INFORMATION); // 기본 다이얼로그 객체 생성
		alert.setTitle("미래 대학교"); // 다이얼로그 타이틀 설정
		alert.setHeaderText("미래 대학교 수강신청 프로그램"); // 헤더 텍스트 설정
		alert.setContentText("Future Univ. Register Courses Version 0.01"); // 컨텐트 텍스트 설정
		alert.setResizable(false); // 리사이즈 불가
		alert.showAndWait(); // 사용자 응답 기다림
		Platform.exit(); // 플랫폼 종료
		
	}

	// 종료 메뉴 이벤트 핸들러
	public void handlerMenuExitAction(ActionEvent event) {
		
		Alert alert;
		alert = new Alert(AlertType.INFORMATION); // 기본 다이얼로그 객체 생성
		alert.setTitle("미래 대학교"); // 다이얼로그 타이틀 설정
		alert.setHeaderText("미래 대학교 수강신청 프로그램 종료"); // 헤더 텍스트 설정
		alert.setContentText("확인 버튼을 클릭하면 미래 대학교 수강신청 프로그램을 종료합니다"); // 컨텐트 텍스트 설정
		alert.setResizable(false); // 리사이즈 불가
		alert.showAndWait(); // 사용자 응답 기다림
		Platform.exit(); // 플랫폼 종료
		
	}

	// 로그아웃 메뉴 이벤트 핸들러
	public void handlerMenuLogoutAction(ActionEvent event) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml")); // 레이아웃 불러오기
			Parent mainView = (Parent)loader.load(); // 부모창을 login.fxml로 로드
			Scene scene = new Scene(mainView); // Scene 객체 생성
			Stage mainStage = new Stage(); // Stage 객체 생성
			mainStage.setTitle("미래 대학교 학사관리"); // 타이틀 설정
			mainStage.setResizable(false); // 리사이즈 불가
			mainStage.setScene(scene); // 씬 설정
			Stage oldStage = (Stage)mainPane.getScene().getWindow(); // 새 스테이지(탭) 추가
			oldStage.close(); // 탭 창 닫음
			mainStage.show(); // 로그인 창 열기
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
