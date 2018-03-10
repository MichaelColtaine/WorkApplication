package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalcController {

	@FXML
	private JFXButton calculateButton;

	@FXML
	private JFXTextField firstInput;

	@FXML
	private JFXTextField secondInput;

	@FXML
	private Label rabatLabel;

	@FXML
	private Label errorLabel;

	@FXML
	void handleCalculateButton(ActionEvent event) {
		String result = "Obě pole musí být vyplněná číslem";

		try {
			result = Model.getInstance().calculate(firstInput.getText(), secondInput.getText());
		} catch (Exception e) {
			System.out.println("Incorrect input");
		}
		showResult(result);
	}

	private void showResult(String result) {
		if (result.equals("Obě pole musí být vyplněná číslem")) {
			errorLabel.setText(result);
			rabatLabel.setText("");
		} else {
			errorLabel.setText("");
			rabatLabel.setText(result);
		}
	}

}
