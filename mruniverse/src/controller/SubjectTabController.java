package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.security.auth.login.LoginContext;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.SubjectVO;

public class SubjectTabController implements Initializable {

	@FXML
	private Label lblManagerName; // 관리자명
	
	@FXML
	private TextField txtSubjectNum; // 학과 번호
	@FXML
	private TextField txtSubjectName; // 학과명
	@FXML
	private Button btnInsert; // 학과 등록 버튼
	@FXML
	private Button btnUpdate; // 학과 수정 버튼
	@FXML
	private Button btnDelete; // 학과 삭제 버튼
	@FXML
	private Button btnRead; // 읽기 버튼
	
	@FXML
	private TableView<SubjectVO> subjectTableView = new TableView<>(); // 학과 목록 테이블
	
	public static ObservableList<SubjectVO> subjectDataList = FXCollections.observableArrayList();
	ObservableList<SubjectVO> selectSubjectVOs = null; // 테이블에서 선택한 정보 저장
	int selectedIndex; // 테이블에서 선택한 학과 정보 인덱스 저장

	public void subjectTotalList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			
			lblManagerName.setText("학과 등록 관리자명 : " + LoginController.managerName);
			
			// 학과 등록 초기화
			btnUpdate.setDisable(true); // 수정 버튼 비활성화
			btnDelete.setDisable(true); // 삭제 버튼 비활성화
			subjectTableView.setEditable(false); // 테이블뷰 수정 금지
			
			// 학과 테이블뷰 컬럼 이름 설정
			TableColumn colNo = new TableColumn("NO");
			colNo.setPrefWidth(50); // 컬럼 넓이 설정
			colNo.setStyle("-fx-alignment:CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			
			TableColumn colSNum = new TableColumn("학과 번호");
			colSNum.setPrefWidth(90); // 컬럼 넓이 설정
			colSNum.setStyle("-fx-alignment:CENTER");
			colSNum.setCellValueFactory(new PropertyValueFactory<>("s_num"));
			
			TableColumn colSName = new TableColumn("학 과 명");
			colSName.setPrefWidth(50); // 컬럼 넓이 설정
			colSName.setStyle("-fx-alignment:CENTER");
			colSName.setCellValueFactory(new PropertyValueFactory<>("s_name"));
			
			subjectTableView.setItems(subjectDataList);
			subjectTableView.getColumns().addAll(colNo, colSNum, colSName);
			
			// 학과 전체 목록
			subjectTotalList();
			
			// 학과 번호 텍스트필드 키 이벤트 핸들러
			txtSubjectNum.setOnKeyPressed(event -> handlerTxtSubjectNoKeyPressed(event));
			
			// 학과 등록, 수정, 삭제 이벤트 등록
			btnInsert.setOnAction(event -> handlerBtnInsertAction(event));
			btnDelete.setOnAction(event -> handlerBtnDeleteAction(event));
			btnUpdate.setOnAction(event -> handlerBtnUpdateAction(event));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 학과 수정 버튼 이벤트 메소드
	public void handlerBtnUpdateAction(ActionEvent event) {
		try {
			boolean sucess;
			
			SubjectDAO sdao = new SubjectDAO();
			sucess = sdao.getSubjectUpdate(selectedIndex, txtSubjectNum.getText().trim(), txtSubjectName.getText().trim());
			
			if(sucess) {
				subjectDataList.removeAll(subjectDataList);
				subjectTotalList(); // 학과 전체 목록 호출
				
				txtSubjectNum.clear(); // 학과 번호 지움
				txtSubjectName.clear(); // 학과명 지움
				btnInsert.setDisable(false); // 등록 버튼 활성화
				btnUpdate.setDisable(true); // 수정 버튼 비활성화
				btnDelete.setDisable(true); // 삭제 버튼 비활성화
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 학과 삭제 버튼 이벤트 메소드
	public void handlerBtnDeleteAction(ActionEvent event) {
		try {
			boolean sucess;
			
			SubjectDAO sdao = new SubjectDAO();
			sucess = sdao.getSubjectDelete(selectedIndex);
			
			if(sucess) {
				subjectDataList.removeAll(subjectDataList);
				subjectTotalList(); // 학과 전체 목록 호출
				
				txtSubjectNum.clear(); // 학과 번호 지움
				txtSubjectName.clear(); // 학과명 지움
				btnInsert.setDisable(false); // 등록 버튼 활성화
				btnUpdate.setDisable(true); // 수정 버튼 비활성화
				btnDelete.setDisable(true); // 삭제 버튼 비활성화
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 학과 등록 버튼 이벤트 메소드
	public void handlerBtnInsertAction(ActionEvent event) {
		
		try {
			subjectDataList.removeAll(subjectDataList);
			
			SubjectVO svo = null;
			SubjectDAO sdao = null;
			
			svo = new SubjectVO(txtSubjectNum.getText().trim(), txtSubjectName.getText().trim());
			sdao = new SubjectDAO();
			sdao.getSubjectRegiste(svo);
			
			if(sdao != null) {
				subjectTotalList(); // 학과 전체 목록 호출
				
				Alert alert = new Alert(AlertType.INFORMATION); // 기본 다이얼로그 객체 생성
				alert.setTitle("학과 입력"); // 다이얼로그 타이틀 설정
				alert.setHeaderText(txtSubjectName.getText() + " 학과가 성공적으로 추가되었습니다"); // 헤더 텍스트 설정
				alert.setContentText("다음 학과를 입력하세요"); // 컨텐트 텍스트 설정
				alert.setResizable(false); // 리사이즈 불가
				alert.showAndWait(); // 사용자 응답 기다림
				
				txtSubjectNum.clear(); // 학과 번호 지움
				txtSubjectName.clear(); // 학과명 지움
				txtSubjectNum.requestFocus(); // 학과 번호 텍스트필드로 포커스 옮김
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING); // 기본 다이얼로그 객체 생성
			alert.setTitle("학과 정보 입력"); // 다이얼로그 타이틀 설정
			alert.setHeaderText("학과 정보를 정확히 입력하세요"); // 헤더 텍스트 설정
			alert.setContentText("다음에는 주의하세요"); // 컨텐트 텍스트 설정
			alert.setResizable(false); // 리사이즈 불가
			alert.showAndWait(); // 사용자 응답 기다림
			Platform.exit(); // 플랫폼 종료
		}
		
	}

	// 
	public void handlerTxtSubjectNoKeyPressed(KeyEvent event) {
		if((txtSubjectNum.getText().length() >= 3)) { // 학과 번호의 길이가 3과 같거나 크면
			txtSubjectNum.clear(); // 학과번호를 지움			
		}
		
		if(event.getCode() == KeyCode.ENTER) { // ENTER키가 입력되면
			txtSubjectName.requestFocus(); // 학과명 텍스트필드로 포커스 옮김
		}
	}

}
