package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.JoinVO;

public class JoinController implements Initializable {
	@FXML
	private TextField txtId; // 아이디 텍스트 상자
	@FXML
	private PasswordField txtPassword; // 비밀번호 텍스트 상자
	@FXML
	private PasswordField txtPasswordRepeat; // 비밀번호 재입력 텍스트 상자
	@FXML
	private TextField txtName; // 이름 텍스트 상자
	@FXML
	private Button btnCancel; // 취소 버튼
	@FXML
	private Button btnJoin; // 등록버튼
	@FXML
	private Button btnOverlap; // 아이디 중복확인버튼

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btnJoin.setDisable(true); // 등록버튼 비활성화
		txtPassword.setEditable(false); // 비밀번호 수정금지
		txtPasswordRepeat.setEditable(false); // 비밀번호 재확인 수정금지

		btnOverlap.setOnAction(event -> handlerBtnOverlapAction(event)); // 아이디 중복 검사 이벤트
		btnJoin.setOnAction(event -> handlerBtnJoinAction(event)); // 관리자 등록 이벤트
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // 등록창 닫기 이벤트

	}

	// 관리자 등록 이벤트
	public void handlerBtnJoinAction(ActionEvent event) {
		// 인스턴스 생성
		JoinVO jvo = null;
		JoinDAO jdao = null;
		// 등록성공여부 판단변수
		boolean joinSucess = false;

		// 패스워드 확인
		// 비밀번호랑 비밀번호 재확인이 같고 이름이 ""아닌경우
		if (txtPassword.getText().trim().equals(txtPasswordRepeat.getText().trim())
				&& !txtName.getText().trim().equals("")) {

			// JoinVO에 입력받은 id, ps, name을 공백제거후 넣어준다
			jvo = new JoinVO(txtId.getText().trim(), txtPassword.getText().trim(), txtName.getText().trim());
			// 인스턴스화
			jdao = new JoinDAO();

			try {
				// JoinDAO에서 managerRegiste메소드를 호출하여 등록후 성공여부를 확인한다.
				joinSucess = jdao.getManagerRegiste(jvo);

				// 등록이 성공하였을 경우
				if (joinSucess) {
					// 취소버튼을 호출하여 창을 닫아준다
					handlerBtnCancelAction(event);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else { // 등록실패

			txtPassword.clear(); // 비밀번호를 지워준다
			txtPasswordRepeat.clear(); // 비밀번호 재확인을 지워준다Alert alert = new Alert(AlertType.WARNING);

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("패스워드, 이름 확인");
			alert.setHeaderText("패스워드, 이름 확인 검사에 오류가 발생");
			alert.setContentText("수고링~");
			alert.showAndWait();

		}

	}

	// 등록창 닫기 이벤트
	public void handlerBtnCancelAction(ActionEvent event) {

		// 등록창을 닫고 로그인 화면을 불러와서 띄운다.
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));

			Parent mainView = (Parent) loader.load();
			Scene scane = new Scene(mainView);
			Stage mainMtage = new Stage();
			mainMtage.setTitle("관리자 로그인");
			mainMtage.setScene(scane);
			Stage oldStage = (Stage) btnJoin.getScene().getWindow();
			oldStage.close();
			mainMtage.show();

		} catch (IOException e) {
			System.out.println("오류 " + e);
		}

	}

	// 아이디 중복 검사 이벤트
	public void handlerBtnOverlapAction(ActionEvent event) {

		btnJoin.setDisable(false); // 등록버튼 활성화
		btnOverlap.setDisable(true); // 아이디 중복확인버튼 비활성화

		// JoinDAO인스턴스 생성
		JoinDAO jDao = null;

		// 입력받을 아이디 변수
		String searchId = "";
		// 검색결과 상태 변수
		boolean searchResult = true;

		try {
			// 입력받은 아이디를 앞뒤 공백제거후 변수에 넣어준다.
			searchId = txtId.getText().trim();
			// 인스턴스화
			jDao = new JoinDAO();
			// joinDAO에서 IdOverlap메소드를 호출하여 중복되는 아이디가 있는지 확인하고 boolean값을 반환받는다.
			searchResult = (boolean) jDao.getIdOverlap(searchId);
			// 검색결과가 false이거나 ""와 같지 않을경우
			if (!searchResult && !searchId.equals("")) {
				txtId.setDisable(true); // 아이디상자 비활성화

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 중복 검사");
				alert.setHeaderText(searchId + " 를 사용할 수 있습니다");
				alert.setContentText("수고링~");
				alert.showAndWait();

				btnJoin.setDisable(false); // 등록버튼 활성화
				btnOverlap.setDisable(true); // 중복검사 버튼 비활성화
				txtPassword.setEditable(true); // 비밀번호 수정가능
				txtPasswordRepeat.setEditable(true); // 비밀번호 확인 수정가능
				txtPassword.requestFocus(); // 비밀번호 입력창으로 포커스를 준다

			} else if (searchId.equals("")) {
				// 아이디가 ""와 같을경우

				btnJoin.setDisable(true); // 등록버튼 비활성화
				btnOverlap.setDisable(false); // 중복검사버튼 활성화

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 중복 검사");
				alert.setHeaderText("아이디를 입력하시오");
				alert.setContentText("수고링~");
				alert.showAndWait();

			} else {
				// 중복검사에서 true이거나 아이디가 공백이 아닐경우

				btnJoin.setDisable(true); // 등록버튼 비활성화
				btnOverlap.setDisable(false); // 중복검사 버튼 활성화
				txtId.clear(); // 입력한 아이디를 지워준다

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 중복 검사");
				alert.setHeaderText(searchId + " 를 사용할 수 없습니다");
				alert.setContentText("수고링~");
				alert.showAndWait();

				// 아이디상자에 포커스를 준다.
				txtId.requestFocus();
			}
		} catch (Exception e) {
			// 경고창
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("아이디 중복검사 오류");
			alert.setHeaderText("중복검사에 오류가 발생하였습니다");
			alert.setContentText("수고링~");
			alert.showAndWait();
		}

	}

}
