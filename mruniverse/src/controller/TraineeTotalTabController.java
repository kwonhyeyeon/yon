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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TraineeVO;

public class TraineeTotalTabController implements Initializable {

	@FXML
	ComboBox<String> cbx_searchList; // 검색 분류 콤보박스
	@FXML
	TextField txtSearchWord; // 검색어 텍스트필드
	@FXML
	Button btnSearch; // 검색 버튼
	@FXML
	Label lblCount; // 총원 라벨
	@FXML
	TableView<TraineeVO> traineeTotalTableView = new TableView<>();

	ObservableList<TraineeVO> traineeDataList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			
			cbx_searchList.setItems(FXCollections.observableArrayList("학번", "과목명","학생이름"));
			
			// 수강 테이블 뷰 컬럼이름 설정
			TableColumn colNo = new TableColumn("NO");
			colNo.setPrefWidth(50);
			colNo.setStyle("-fx-alignment:CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

			TableColumn colSdNum = new TableColumn("학번");
			colSdNum.setPrefWidth(150);
			colSdNum.setStyle("-fx-alignment:CENTER");
			colSdNum.setCellValueFactory(new PropertyValueFactory<>("sd_num"));

			TableColumn colSdName = new TableColumn("학생 이름");
			colSdName.setPrefWidth(150);
			colSdName.setStyle("-fx-alignment:CENTER");
			colSdName.setCellValueFactory(new PropertyValueFactory<>("sd_name"));

			TableColumn colLNum = new TableColumn("과목명");
			colLNum.setPrefWidth(150);
			colLNum.setStyle("-fx-alignment:CENTER");
			colLNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));

			TableColumn colTSection = new TableColumn("과목 구분");
			colTSection.setPrefWidth(150);
			colTSection.setStyle("-fx-alignment:CENTER");
			colTSection.setCellValueFactory(new PropertyValueFactory<>("t_section"));

			TableColumn colTDate = new TableColumn("등록 날짜");
			colTDate.setPrefWidth(250);
			colTDate.setStyle("-fx-alignment:CENTER");
			colTDate.setCellValueFactory(new PropertyValueFactory<>("t_date"));

			traineeTotalTableView.setItems(traineeDataList);
			traineeTotalTableView.getColumns().addAll(colNo, colSdNum, colSdName, colLNum, colTSection, colTDate);
			// 수강 전체 목록
			traineeTotalList();
			// 검색 버튼 이벤트 핸들러
			btnSearch.setOnAction(event -> handlerBtnSearchAction(event));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 검색 버튼 이벤트 핸들러 메소드
	public void handlerBtnSearchAction(ActionEvent event) {

		String search = "";
		search = cbx_searchList.getSelectionModel().getSelectedItem();

		TraineeVO tVo = new TraineeVO();
		TraineeDAO tDao = new TraineeDAO();

		String searchName = "";
		boolean searchResult = false;

		searchName = txtSearchWord.getText().trim(); // 검색어 필드에서 공백을 뺀 값을 가져와 저장한다

		try {

			// 검색어 필드 값이 공백이거나 콤보박스에서 아무것도 선택하지 않았을 때
			if (searchName.equals("") || search.equals("")) {
				try {
					searchResult = true;

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("수강 정보 검색");
					alert.setHeaderText("수강 검색 정보를 입력하세요");
					alert.setContentText("다음에는 주의하세요");
					alert.showAndWait();

					traineeTotalList(); // 수강 전체 목록 메소드 호출
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				ArrayList<String> title;
				ArrayList<TraineeVO> list = null;

				title = tDao.getTraineeColumnName();
				int columnCount = title.size();
				
				if (search.equals("학번")) { // 콤보박스에서 학번을 선택했을 때
					list = tDao.getTraineeStudentNumSearchList(searchName);
					if (list.size() == 0) { // 값이 없을 때
						txtSearchWord.clear(); // 검색어 필드 초기화

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("학생 학번 정보 검색");
						alert.setHeaderText(searchName + " 학번의 수강 리스트에 없습니다");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();

						list = tDao.getTraineeTotalList();
					}
				}

				if (search.equals("과목명")) { // 콤보박스에서 과목명을 선택했을 때
					list = tDao.getTraineeSubjectSearchList(searchName);

					if (list.size() == 0) { // 값이 없을 때
						list = tDao.getTraineeTotalList();
					}
				}

				if (search.equals("학생이름")) { // 콤보박스에서 학번을 선택했을 때
					list = tDao.getTraineeStudentNameSearchList(searchName);

					if (list.size() == 0) { // 값이 없을 때
						txtSearchWord.clear(); // 검색어 필드 초기화

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("학생 이름 정보 검색");
						alert.setHeaderText(searchName + " 이름의 수강 리스트에 없습니다");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();

						list = tDao.getTraineeTotalList();
					}
				}

				txtSearchWord.clear();
				traineeDataList.removeAll(traineeDataList);

				int rowCount = list.size();
				lblCount.setText("검색 : " + rowCount + " 명");

				for (int index = 0; index < rowCount; index++) {
					tVo = list.get(index);
					traineeDataList.add(tVo);
				}

				searchResult = true;

			}
			
			if(!searchResult) {
				txtSearchWord.clear();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("학생 정보 검색");
				alert.setHeaderText(searchName + " 학생이 리스트에 없습니다");
				alert.setContentText("다시 검색하세요");
				alert.showAndWait();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 수강 전체 리스트
	public void traineeTotalList() throws Exception {
		
		traineeDataList.removeAll(traineeDataList);
		
		TraineeDAO tDao = new TraineeDAO();
		TraineeVO tVo = null;
		
		ArrayList<String> title;
		ArrayList<TraineeVO> list;
		
		title = tDao.getTraineeColumnName();
		int columnCount = title.size();
		
		list = tDao.getTraineeTotalList();		
		int rowCount = list.size();
		
        lblCount.setText("수강 신청\t\t\t총원 : " + rowCount + " 명");
		for(int index = 0; index < rowCount; index++) {
			tVo = list.get(index);
			traineeDataList.add(tVo);
			
		}
	}

}
