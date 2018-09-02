package application.analysis;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jfoenix.controls.JFXButton;

import application.shared.ArticleRow;
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

	@FXML
	public void initialize() {
		ipLabel.setText("IP adresa tohoto poèítaèe: " + getIp());
		AnalysisSender.getInstance().listen(serverInfoLabel);
		ExportReceiver.getInstance().listen(databaseLabel);
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				AnalysisModel.getInstance().setData(readDataFile());
			}
		});
		t1.start();
	}

	private HashMap<String, ArticleRow> readDataFile() {
		HashMap<String, ArticleRow> map = new HashMap<>();
		File file = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "data.xlsx");

		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				try {
					String ean = row.getCell(0).toString();
					String name = row.getCell(1).toString();
					map.put(ean, new ArticleRow(ean, name, "", "", "", "", "", "", ""));
				} catch (NullPointerException e) {
					System.out.println("Nullpointer, jedna bunka je prazdna. AnalysisController");
					continue;
				}
			}
			wb.close();

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data size " + map.size());
		return map;
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
		AnalysisModel.getInstance().setAnalysis(readAnalysisFile(files.get(0)));
	}

	private HashMap<String, ArticleRow> readAnalysisFile(File f) {
		HashMap<String, ArticleRow> list = new HashMap<>();
		try {
			Workbook wb = WorkbookFactory.create(f);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				try {

					String ean = row.getCell(0).toString();
					String name = row.getCell(1).toString();
					String sales = row.getCell(2).toString();
					String amount = row.getCell(3).toString();
					String price = row.getCell(4).toString();
					String supplier = row.getCell(5).toString();
					String lastSale = row.getCell(6).toString();
					String lastDelivery = row.getCell(7).toString();
					String deliveredAS = row.getCell(8).toString(); // 1512 = sale, 1514 = Consignment
					list.put(ean, new ArticleRow(ean, name, sales, amount, price, supplier, lastSale, lastDelivery,
							deliveredAS));
				} catch (NullPointerException e) {
					System.out.println("Nullpointer, jedna bunka je prazdna. AnalysisController");
					continue;
				}
			}
			wb.close();

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Analysis size" + list.size());
		return list;
	}

	@FXML
	private void handleDeleteButtonAction(ActionEvent event) {
		if (files != null || !files.isEmpty()) {
			AnalysisModel.getInstance().getData().clear();
			files.clear();
			fileNameLabel.setText("");
		}
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
