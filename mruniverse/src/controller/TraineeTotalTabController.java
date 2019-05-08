package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
			
			if() {
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void traineeTotalList() {
		// TODO Auto-generated method stub
		
	}

}
