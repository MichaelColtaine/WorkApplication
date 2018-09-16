package application.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import application.shared.ArticleRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
	private Label errorLabel;

	@FXML
	private ProgressIndicator progressBar;

	@FXML
	public void initialize() {
		ipLabel.setText("IP adresa tohoto poèítaèe: " + getIp());
		AnalysisSender.getInstance().listen(serverInfoLabel, progressBar);
		ExportReceiver.getInstance().listen(databaseLabel);
		System.out.println(files.size());
	}

	@FXML
	private void handleDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	private List<File> files = new ArrayList<>();

	@FXML
	private void handleDrop(DragEvent event) {
		InfoModel.getInstance().updateInfo("");
		errorLabel.setText("");
		AnalysisModel.getInstance().getAnalysis().clear();
		if (!files.isEmpty()) {
			files.clear();
		}

		files = event.getDragboard().getFiles();
		if (files.get(0).getName().toLowerCase().contains(".csv")) {
			fileNameLabel.setText("Vložen soubor " + files.get(0).getName());
			progressBar.setVisible(true);
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					InfoModel.getInstance().updateInfo("Nahrávám data z analýzy...");
					AnalysisModel.getInstance().setAnalysis(readAnalysisFile(files.get(0)));
					InfoModel.getInstance().updateInfo("Nahrávám data...");
					progressBar.setVisible(false);
					InfoModel.getInstance().updateInfo("Hotovo");
				}
			});
			t1.start();

		} else {
			errorLabel.setText("Musíte vložit soubor typu .csv");
		}

	}

	// private HashMap<String, ArticleRow> readDataFile() {
	// HashMap<String, ArticleRow> map = new HashMap<>();
	// File file = new File(System.getProperty("user.dir") + File.separator + "data"
	// + File.separator + "data.xlsx");
	// try {
	// Workbook wb = WorkbookFactory.create(file);
	// Sheet sheet = wb.getSheetAt(0);
	// sheet.removeRow(sheet.getRow(0));
	// String name = "", ean = "";
	// for (Row row : sheet) {
	// try {
	// ean = row.getCell(0).toString();
	// name = row.getCell(1).toString();
	// map.put(ean, new ArticleRow(ean, name, "", "", "", "", "", "", ""));
	// } catch (NullPointerException e) {
	// System.out.println("Nullpointer, jedna bunka je prazdna. readDataFile");
	// continue;
	// }
	// }
	// wb.close();
	//
	// } catch (EncryptedDocumentException e) {
	// reset();
	// e.printStackTrace();
	// } catch (InvalidFormatException e) {
	// reset();
	// e.printStackTrace();
	// } catch (IOException e) {
	// reset();
	// e.printStackTrace();
	// }
	// return map;
	// }

	boolean isFirstLine = true;

	private HashMap<String, ArticleRow> readAnalysisFile(File f) {
		HashMap<String, ArticleRow> map = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
				try {
					line = cleanString(line);
					String[] row = line.split(";");

					String firstDate = convertDate(Double.valueOf(row[6]));
					String secondDate = row[7];
					try {
						System.out.println("SECOND DATE " + row[0] + " " + secondDate + " delka " + secondDate.length());
						secondDate = secondDate.substring(0, 5);
					} catch (Exception e) {
						System.out.println(row[0] + " " + row[6] + " " + row[7]);
						e.printStackTrace();
						continue;
						
					}
					secondDate = convertDate(Double.valueOf(secondDate));
					System.out.println(row[0]);
					System.out.println(firstDate);
					System.out.println(secondDate);
					System.out.println();
					
					if (row.length == 9) {
						map.put(row[0],
								new ArticleRow(row[0], row[1], row[2], row[3], row[4], row[5], firstDate, secondDate, row[8]));
					} else if (row.length == 11) {
						map.put(row[0], new ArticleRow(row[0], row[1], row[4], row[5], firstDate, secondDate, row[8], row[9],
								row[10]));
					} else {
						System.out.println(row[0] + " " + row[1] + " row length " + row.length);
						continue;
					}

//					if (row.length == 9) {
//						map.put(row[0],
//								new ArticleRow(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8]));
//					} else if (row.length == 11) {
//						map.put(row[0], new ArticleRow(row[0], row[1], row[4], row[5], row[6], row[7], row[8], row[9],
//								row[10]));
//					} else {
//						System.out.println(row[0] + " " + row[1] + " row length " + row.length);
//						continue;
//					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	private String cleanString(String s) {
		return s.replaceAll("\"", "").replaceAll("&quot", "");
	}

	private String convertDate(Double d) {
		Date javaDate = DateUtil.getJavaDate((double) d);
		return new SimpleDateFormat("dd/MM/yyyy").format(javaDate);
	}

	private void reset() {
		if (AnalysisModel.getInstance().getAnalysis() != null) {
			AnalysisModel.getInstance().getAnalysis().clear();
		}
	}

	@FXML
	private void handleDeleteButtonAction(ActionEvent event) {
		if (files != null || !files.isEmpty()) {
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

}
