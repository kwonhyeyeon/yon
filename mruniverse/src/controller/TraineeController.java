package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.StudentVO;
import model.TraineeVO;

public class TraineeController implements Initializable {

	// 메뉴
	@FXML
	private MenuItem menuLogout; // 로그아웃 메뉴
	@FXML
	private MenuItem menuExit; // 종료 메뉴
	@FXML
	private MenuItem menuInfo; // 프로그램 정보 메뉴

	// 수강 신청 탭
	@FXML
	private TextField txtStudentName; // 학생 이름
	@FXML
	private TextField txtStudentNum; // 학번
	@FXML
	private TextField txtSubjectName; // 학과명
	@FXML
	private RadioButton rbMajor; // 전공 라디오버튼
	@FXML
	private RadioButton rbMinor; // 부전공 라디오버튼
	@FXML
	private RadioButton rbCulture; // 교양 라디오버튼
	@FXML
	private ComboBox<String> cbx_subjectName; // 과목 선택 콤보박스
	@FXML
	private TextField txtSectionName; // 과목 선택 텍스트필드
	@FXML
	private Button btnTraineeInsert; // 수강신청 버튼
	@FXML
	private Button btnTraineeCancle; // 수강취소 버튼
	@FXML
	private Button btnTraineeExit; // 종료 버튼
	@FXML
	private TableView<TraineeVO> traineeTableView = new TableView<>();

	ObservableList<TraineeVO> traineeDataList = FXCollections.observableArrayList();
	ObservableList<TraineeVO> selectTrainee = null; // 테이블에서 선택한 정보 저장

	int selectedTraineeIndex; // 테이블에서 선택한 수강 신청 인덱스 저장

	String studentLoginId; // 로그인 아이디
	String l_num; // 과목 번호
	String t_section; // 과목 구분
	String sd_num; // 로그인한 학생의 학번

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			studentLoginId = LoginController.studentId; // 로그인 컨트롤러의 학생 아이디를 저장

			if (!studentLoginId.equals("")) { // 로그인 아이디가 들어오면

				StudentVO sVo = new StudentVO();
				TraineeDAO tDao = new TraineeDAO();
				sVo = tDao.getStudentSubjectName(studentLoginId); // 로그인한 해당 학생의 정보를 sVo에 저장

				// 각 필드에 sVo에서 읽어온 값을 설정해준다
				txtStudentNum.setText(sVo.getSd_num());
				txtStudentName.setText(sVo.getSd_name());
				txtSubjectName.setText(sVo.getS_num());

				sd_num = txtStudentNum.getText().trim(); // 공백을 제거한 학번의 값을 sd_num에 저장

				btnTraineeCancle.setDisable(true); // 수강 취소 버튼 비활성화
				traineeTableView.setEditable(false); // 테이블뷰 수정 불가

				// 학생 정보 수정 금지
				txtStudentName.setEditable(false);
				txtStudentNum.setEditable(false);
				txtSubjectName.setEditable(false);
				txtSectionName.setEditable(false);

				// 수강 테이블뷰 컬럼이름 설정
				TableColumn colNo = new TableColumn("NO");
				colNo.setPrefWidth(50); // 컬럼 넓이 설정
				colNo.setStyle("-fx-alignment:CENTER");
				colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

				TableColumn colSdNum = new TableColumn("학번");
				colSdNum.setPrefWidth(90); // 컬럼 넓이 설정
				colSdNum.setStyle("-fx-alignment:CENTER");
				colSdNum.setCellValueFactory(new PropertyValueFactory<>("sd_num"));

				TableColumn colLNum = new TableColumn("과목명");
				colLNum.setPrefWidth(100); // 컬럼 넓이 설정
				colLNum.setStyle("-fx-alignment:CENTER");
				colLNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));

				TableColumn colTSection = new TableColumn("과목 구분");
				colTSection.setPrefWidth(100); // 컬럼 넓이 설정
				colTSection.setStyle("-fx-alignment:CENTER");
				colTSection.setCellValueFactory(new PropertyValueFactory<>("t_section"));

				TableColumn colTDate = new TableColumn("등록 날짜");
				colTDate.setPrefWidth(160); // 컬럼 넓이 설정
				colTDate.setStyle("-fx-alignment:CENTER");
				colTDate.setCellValueFactory(new PropertyValueFactory<>("t_date"));

				traineeTableView.setItems(traineeDataList);
				traineeTableView.getColumns().addAll(colNo, colSdNum, colLNum, colTSection, colTDate);

				// 수강 전체 목록
				traineeTotalList();

				// 로그아웃 버튼 이벤트 등록
				menuLogout.setOnAction(event -> handlerMenuLogoutAction(event));
				// 종료 버튼 이벤트 등록
				menuExit.setOnAction(event -> handlerMenuExitAction(event));
				// 프로그램 정보 이벤트 등록
				menuInfo.setOnAction(event -> handlerMenuInfoAction(event));

				// 수강 과목 선택 이벤트
				rbMajor.setOnAction(event -> handlerRbMajorAction(event));
				rbMinor.setOnAction(event -> handlerRbMinorAction(event));
				rbCulture.setOnAction(event -> handlerRbCultureAction(event));
				
				// 학생 등록 탭의 학과 선택 이벤트 핸들러
				cbx_subjectName.setOnAction(event -> handlerCbx_SubjectName(event));

				// 수강 등록, 취소 버튼 이벤트 등록
				btnTraineeInsert.setOnAction(event -> handlerBtnTraineeInsertAction(event));
				btnTraineeCancle.setOnAction(event -> handlerBtnTraineeCancleAction(event));

				// 종료 버튼 이벤트 등록
				btnTraineeExit.setOnAction(event -> handlerBtnTraineeExitAction(event));

				// 테이블 뷰 더블 클릭 이벤트 등록
				traineeTableView.setOnMouseClicked(event -> handlerTraineeTableViewAction(event));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// 학생 등록 탭의 학과 선택 이벤트 메소드
	public void handlerCbx_SubjectName(ActionEvent event) {
		
		txtSectionName.setText(cbx_subjectName.getSelectionModel().getSelectedItem());
		selectLessonNameToLessonNum();
		
	}

	// 수강 전체 리스트
	public void traineeTotalList() throws Exception {
		
		TraineeDAO tDao = new TraineeDAO();
		TraineeVO tVo = null;
		ArrayList<String> title;
		ArrayList<TraineeVO> list;
		
		title = tDao.getTraineeColumnName();
		int columnCount = title.size();
		
		list = tDao.getTraineeTotalList(sd_num);
		int rowCount =  list.size();
		
		for(int index = 0; index < rowCount; index ++) {
			tVo = list.get(index);
			traineeDataList.add(tVo);
		}
		
	}
	
	// 수강 신청한 과목명의 과목 번호
	public void selectLessonNameToLessonNum() {
		
		try {
			TraineeDAO tDao = new TraineeDAO();
			
			if(txtSectionName.getText() != null) {
				l_num = tDao.getLessonNum(txtSectionName.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 수강 테이블뷰 더블클릭 선택 이벤트 메소드
	public void handlerTraineeTableViewAction(MouseEvent event) {

		if (event.getClickCount() == 2) { // 더블 클릭 하면

			try {
				selectTrainee = traineeTableView.getSelectionModel().getSelectedItems();
				
				selectedTraineeIndex = selectTrainee.get(0).getNo();
				String selectedL_num = selectTrainee.get(0).getL_num();
				
				txtSectionName.setText(selectedL_num);
				
				btnTraineeCancle.setDisable(false); // 수강 취소 버튼 활성화
				btnTraineeInsert.setDisable(true); // 수강 등록 버튼 비활성화
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// 종료 버튼 이벤트 메소드
	public void handlerBtnTraineeExitAction(ActionEvent event) {
		Platform.exit();
	}

	// 수강 신청 취소 버튼 이벤트 메소드
	public void handlerBtnTraineeCancleAction(ActionEvent event) {

		try {

			boolean sucess;

			TraineeDAO tdao = new TraineeDAO();
			sucess = tdao.getTraineeDelete(selectedTraineeIndex);

			if (sucess) {

				traineeDataList.removeAll(traineeDataList);
				traineeTotalList();

				btnTraineeCancle.setDisable(true); // 취소 버튼 비활성화
				btnTraineeInsert.setDisable(false); // 등록 버튼 활성화

				txtSectionName.clear();
				l_num = "";
				t_section = "";
				rbCulture.setSelected(false);
				rbMajor.setSelected(false);
				rbMinor.setSelected(false);
				cbx_subjectName.setItems(FXCollections.observableArrayList("선택"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 수강 신청 등록 버튼 이벤트 메소드
	public void handlerBtnTraineeInsertAction(ActionEvent event) {

		try {

			traineeDataList.removeAll(traineeDataList);

			TraineeVO tvo = null;
			TraineeDAO tdao = null;

			tvo = new TraineeVO(txtStudentNum.getText().trim(), l_num, t_section);
			tdao = new TraineeDAO();
			tdao.getTraineeRegiste(tvo);

			if (tdao != null) {

				traineeTotalList(); // 수강 신청 전체 목록 메소드 호출

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("수강 신청");
				alert.setHeaderText(txtStudentName.getText() + " 수강 신청이 되었습니다");
				alert.setContentText("다른 과목 수강 신청을 하세요");
				alert.setResizable(false);
				alert.showAndWait();

				txtSectionName.clear();
				l_num = "";
				t_section = "";
				rbCulture.setSelected(false);
				rbMajor.setSelected(false);
				rbMinor.setSelected(false);
				cbx_subjectName.setItems(FXCollections.observableArrayList("선택"));

			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("학과 정보 입력");
			alert.setHeaderText("학과 정보를 정확히 입력하세요");
			alert.setContentText("다음에는 주의하세요!");
			alert.setResizable(false);
			alert.showAndWait();
		}

	}

	// 전공 라디오버튼 이벤트 메소드
	public void handlerRbCultureAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("국어", "수학", "영어", "역사"));
		t_section = rbCulture.getText();
	}

	// 전공 라디오버튼 이벤트 메소드
	public void handlerRbMinorAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("교육학이론"));
		t_section = rbMinor.getText();
	}

	// 전공 라디오버튼 이벤트 메소드
	public void handlerRbMajorAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("프로그래밍", "데이터베이스"));
		t_section = rbMajor.getText();
	}

	// 프로그램 정보 메뉴 이벤트 메소드
	public void handlerMenuInfoAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("미래 대학교");
		alert.setHeaderText("미래 대학교 수강신청 프로그램");
		alert.setContentText("Future Univ. Register Courses Version 0.01");
		alert.setResizable(false);
		alert.showAndWait();
	}

	// 종료 버튼 이벤트 메소드
	public void handlerMenuExitAction(ActionEvent event) {
		Platform.exit();
	}

	// 로그아웃 버튼 이벤트 메소드
	public void handlerMenuLogoutAction(ActionEvent event) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml")); // 레이아웃 불러오기
			Parent mainView = (Parent) loader.load(); // 부모창을 login.fxml로 로드
			Scene scene = new Scene(mainView); // Scene 객체 생성
			Stage mainStage = new Stage(); // Stage 객체 생성
			mainStage.setTitle("미래 대학교 학사관리"); // 타이틀 설정
			mainStage.setResizable(false); // 리사이즈 불가
			mainStage.setScene(scene); // 씬 설정
			Stage oldStage = (Stage) btnTraineeExit.getScene().getWindow(); // 새 스테이지(탭) 추가
			oldStage.close(); // 탭 창 닫음
			mainStage.show(); // 로그인 창 열기

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
