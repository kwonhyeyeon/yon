package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.LessonVO;

public class LessonTabController implements Initializable {
	// 과목 등록 탭
	@FXML
	private TextField txtLessonNum; // 과목번호 입력상자
	@FXML
	private TextField txtLessonName; // 과목명 입력상자
	@FXML // 테이블뷰 (LessonVO)타입으로 인스턴스화
	private TableView<LessonVO> lessonTableView = new TableView<>();
	@FXML
	private Button btnLessonInsert; // 과목등록 버튼
	@FXML
	private Button btnLessonUpdate; // 과목 수정 버튼
	@FXML
	private Button btnLessonDelete; // 과목 삭제 버튼

	// 객체배열 생성
	ObservableList<LessonVO> lessonDataList = FXCollections.observableArrayList();
	ObservableList<LessonVO> selectLesson = null; // 테이블에서 선택한 정보 저장
	int selectedLessonIndex; // 테이블에서 선택한 과목 정보 인덱스 저장

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			System.out.println("aa");
			// 과목등록 초기화
			btnLessonUpdate.setDisable(true); // 과목 수정버튼 비활성화
			btnLessonDelete.setDisable(true); // 과목 삭제버튼 비활성화
			lessonTableView.setEditable(false); // 테이블뷰 수정금지

			// 과목 테이블 뷰 컬럼이름 설정
			TableColumn colLessonNo = new TableColumn("NO"); // 컬럼 이름설정
			colLessonNo.setPrefWidth(50); // 컬럼 크기설정
			colLessonNo.setStyle("-fx-alignment:CENTER"); // 컬럼 가운데 정렬
			colLessonNo.setCellValueFactory(new PropertyValueFactory<>("no")); // 뭥미
			TableColumn colLessonNum = new TableColumn("과목번호"); // 테이블뷰 이름설정
			colLessonNum.setPrefWidth(90);
			colLessonNum.setStyle("-fx-alignment:CENTER");
			colLessonNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));
			TableColumn colLessonName = new TableColumn("과목명");
			colLessonName.setPrefWidth(160);
			colLessonName.setStyle("-fx-alignment:CENTER");
			colLessonName.setCellValueFactory(new PropertyValueFactory<>("l_name"));

			// 과목 테이블뷰에 dataList를 가져와서 넣어준다.
			lessonTableView.setItems(lessonDataList);
			// 과목테이블뷰에 모든 데이터를 추가한다.
			lessonTableView.getColumns().addAll(colLessonNo, colLessonNum, colLessonName);
			
			System.out.println("cc");

			// 과목 전체 목록
			lessonTotalList();
			System.out.println("bb");
			// 과목 등록 텍스트 필드키 이벤트 핸들러
			txtLessonNum.setOnKeyPressed(event -> handlerTxtLessonNumKeyPressed(event));
			// 과목 등록, 수정, 삭제 이벤트 등록
			btnLessonInsert.setOnAction(event -> handlerBtnLessonInsertAction(event));
			btnLessonDelete.setOnAction(event -> handlerBtnLessonDeleteAction(event));
			btnLessonUpdate.setOnAction(event -> handlerBtnLessonUpdateAction(event));

			lessonTableView.setOnMouseClicked(event -> handlerLessonTableViewAction(event));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 과목 테이블뷰 더블클릭 선택 이벤트 핸들러
	public void handlerLessonTableViewAction(MouseEvent event) {
		// TODO Auto-generated method stub
		if (event.getClickCount() == 2) {

			try {
				// 선택된 과목 = 테이블뷰에서 선택된 모델의 선택된 아이템
				selectLesson = lessonTableView.getSelectionModel().getSelectedItems();

				selectedLessonIndex = selectLesson.get(0).getNo();
				String selectedL_num = selectLesson.get(0).getL_num();
				String selectedL_name = selectLesson.get(0).getL_name();

				txtLessonNum.setText(selectedL_num); // 과목번호 상자에 선택된 과목번호를 넣어준다
				txtLessonName.setText(selectedL_name); // 과목명 상자에 선택된 과목명을 넣어준다.

				btnLessonUpdate.setDisable(false); // 수정버튼 활성화
				btnLessonDelete.setDisable(false); // 삭제버튼 활성화
				btnLessonInsert.setDisable(true); // 등록버튼 비활성화

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// 과목 수정 이벤트 핸들러
	public void handlerBtnLessonUpdateAction(ActionEvent event) {
		// TODO Auto-generated method stub

		try {
			// 수정 성공 판단 변수
			boolean sucess;
			// 인스턴스 생성
			LessonDAO ldao = new LessonDAO();
			// idao에 수정메소드에 공백제거후 선택된 인덱스번호, 과목번호, 과목명을 공백제거후 넣어준후 성공여부 boolean값을 반환받는다
			sucess = ldao.getLessonUpdate(selectedLessonIndex, txtLessonNum.getText().trim(),
					txtLessonName.getText().trim());
			// 수정 성공시
			if (sucess) {
				// 원래 있던 정보를 지우고 반환한다.
				lessonDataList.removeAll(lessonDataList);
				lessonTotalList();

				// 과목번호, 과목명 상자를 지워준다
				txtLessonNum.clear();
				txtLessonName.clear();

				btnLessonInsert.setDisable(false); // 등록버튼 활성화
				btnLessonUpdate.setDisable(true); // 수정버튼 비활성화
				btnLessonDelete.setDisable(true); // 삭제버튼 비활성화
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 과목 전체 목록
	public void lessonTotalList() throws Exception {
		System.out.println("zz");
		// 삭제후 반환한다
		lessonDataList.removeAll(lessonDataList);
		// LessonDAO객체 인스턴스화
		LessonDAO lDao = new LessonDAO();
		// 객체 인스턴스 선언
		LessonVO lVo = null;
		// 배열생성
		ArrayList<String> title = null;
		ArrayList<LessonVO> list = null;
		title = lDao.getLessonColumnName();
		// 컬럼갯수
		int columnCount = title.size();
		list = lDao.getLessonTotalList();
		// 행의 갯수
		int rowCount = list.size();
		// 행의 갯수만큼 반복하여 lessonDataList배열에 객체를 추가시킨다.
		for (int index = 0; index < rowCount; index++) {
			lVo = list.get(index);
			lessonDataList.add(lVo);
		}

	}

	// 과목 삭제 이벤트 핸들러
	public void handlerBtnLessonDeleteAction(ActionEvent event) {
		// TODO Auto-generated method stub
		try {
			// 삭제 성공 판단변수
			boolean sucess;
			// LessonDAO객체 인스턴스화
			LessonDAO ldao = new LessonDAO();
			// ldao에서 LessonDelete메소드에 선택된 인덱스 번호를 넣고 행을 삭제한후 성공여부를 반환받는다
			sucess = ldao.getLessonDelete(selectedLessonIndex);
			// true일 경우
			if (sucess) {
				// 선택된 행의 정보를 삭제후 반환한다.
				lessonDataList.removeAll(lessonDataList);
				lessonTotalList();
				// 과목번호, 과목명을 지워준다
				txtLessonNum.clear();
				txtLessonName.clear();
				btnLessonInsert.setDisable(false); // 과목등록버튼 활성화
				btnLessonUpdate.setDisable(true); // 수정버튼 비활성화
				btnLessonDelete.setDisable(true); // 삭제버튼 비활성화
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 과목 등록 이벤트 핸들러
	public void handlerBtnLessonInsertAction(ActionEvent event) {
		// TODO Auto-generated method stub
		try {
			// DataList를 삭제후 반환한다.
			lessonDataList.removeAll(lessonDataList);
			// 객체 인스턴스선언
			LessonVO lvo = null;
			LessonDAO ldao = null;
			// lvo객체에 입력받은 과목번호, 과목명을 공백제거후 넣어주고 인스턴스화한다.
			lvo = new LessonVO(txtLessonNum.getText().trim(), txtLessonName.getText().trim());
			// ldao객체 인스턴스화
			ldao = new LessonDAO();
			// ldao에 과목등록 메소드에 ivo객체를 넣어 과목을 등록한다.
			ldao.getLessonRegiste(lvo);

			if (ldao != null) {
				lessonTotalList();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 입력");
				alert.setHeaderText(txtLessonName.getText() + "과목이 성공적으로 추가되었습니다");
				alert.setContentText("수고링~");
				alert.showAndWait();
				// 과목번호, 과목명을 지워준다
				txtLessonNum.clear();
				txtLessonName.clear();
				// 과목번호로 포커스를 준다.
				txtLessonNum.requestFocus();

			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("과목 정보 입력");
			alert.setHeaderText("과목정보를 정확하게 입력하시오");
			alert.setContentText("수고링~");
			alert.showAndWait();
		}

	}

	// 과목 등록 텍스트 필드키 이벤트 핸들러
	public void handlerTxtLessonNumKeyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		// 과목등록시 과목번호의 길이가 3자리 이상일경우 입력한걸 지워준다.
		if (txtLessonNum.getText().length() >= 3) {
			txtLessonNum.clear();
		}
		// 엔터키 발생시 과목명으로 포커스를 준다.
		if (event.getCode() == KeyCode.ENTER) {
			txtLessonName.requestFocus();
		}
	}

}
