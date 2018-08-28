package application.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AnalysisController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private Label serverInfoLabel;

	@FXML
	private Label fileNameLabel;

	@FXML
	private Label ipLabel;

	@FXML
	private ImageView imageView;

	@FXML
	private JFXButton deleteButton;

	@FXML
	private Label databaseLabel;

	private String data = "";

	@FXML
	public void initialize() {
		ipLabel.setText("IP adresa tohoto poèítaèe: " + getIp());
		DataSender.getInstance().listen(serverInfoLabel);
		DataReceiver.getInstance().listen();

	}

	private String loadDataOfAllBooks() {
		String[] temp;
		StringBuilder sb = new StringBuilder();
		File file = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "data.csv");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "CP1250"))) {

			String line = "";
			while ((line = br.readLine()) != null) {

				temp = line.split(",");
				if (temp.length < 2) {
					continue;
				}

				if (temp[1].length() > 30) {
					String name = temp[1];
					temp[1] = name.substring(0, 30);

				}
				sb.append(temp[0]).append(";").append(temp[1]).append(";").append("nic").append(";").append("nic")
						.append(";").append("nic").append(";").append("nic").append(";").append("nic").append(";")
						.append("nic").append(";").append("nic").append(";").append("~");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getIp() {
		String ip = "";
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	@FXML
	private void handleDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	private List<File> files;

	@FXML
	private void handleDrop(DragEvent event) {
		files = event.getDragboard().getFiles();
		fileNameLabel.setText("Vložen soubor " + files.get(0).getName());
		AnalysisModel.getInstance().setData(createStringFromFile(files.get(0)));
	}

	

	@FXML
	private void handleDeleteButtonAction(ActionEvent event) {
		if (files != null || !files.isEmpty()) {
			AnalysisModel.getInstance().setData("");
			files.clear();
			fileNameLabel.setText("");
		}
	}

	// it reads all the lines in file and converts them to string.
	private String createStringFromFile(File f) {
		StringBuilder sb = new StringBuilder();
		boolean firstLine = true;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "CP1250"))) {
			String line = "";
			while ((line = br.readLine()) != null) {

				// first line is just a header so we can skip it
				if (firstLine) {
					firstLine = false;
					continue;
				}

				// Shortens book names (Too long names causes a bug)
				String[] temp = line.split(";");
				if (temp[1].length() > 30) {
					String name = temp[1];
					temp[1] = name.substring(0, 30);

				}

				StringBuilder tmp = new StringBuilder();
				for (String s : temp) {
					tmp.append(s).append(";");
				}

				sb.append(tmp.toString().toLowerCase()).append("~");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AnalysisSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
