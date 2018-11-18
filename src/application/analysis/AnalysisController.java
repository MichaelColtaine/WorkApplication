package application.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jfoenix.controls.JFXButton;
import com.mtr.application.shared.ArticleRow;
import com.mtr.application.shared.Message;

import application.Model;
import application.infobar.InfoModel;
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

		} else if (files.get(0).getName().toLowerCase().contains(".xls")) {
			fileNameLabel.setText("Vložen soubor " + files.get(0).getName());
			progressBar.setVisible(true);
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					InfoModel.getInstance().updateInfo("Nahrávám data z analýzy...");
					AnalysisModel.getInstance().setAnalysis(readSSBAnalysis(files.get(0)));
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
					String rank = row[0];
					String firstCode = row[1];
					String ean = row[2];
					String name = row[3];
					String sales = row[4];
					String sales2 = row[5];
					String revenue = row[6];
					String storedAmount = row[7];
					String supplies = row[8];
					String locations = row[9];
					String price = row[10];
					String dph = row[11];
					String supplier = row[12];
					String author = row[13];
					String dateOfLastSale = row[14];
					String dateOfLastDelivery = row[15];
					String releaseDate = row[16];
					String deliveredAs = row[17];
					String eshopRank = row[18];
					String analysisDate = row[20];

					if (row.length != 25) {
						System.out.println(row.length + " " + ean);
					}

					if (row.length == 27) {
						dateOfLastSale = row[16];
						dateOfLastDelivery = row[17];
						releaseDate = row[18];
						deliveredAs = row[19];
						eshopRank = row[20];
						analysisDate = row[22];

					} else if (row.length == 26) {
						dateOfLastSale = row[15];
						dateOfLastDelivery = row[16];
						releaseDate = row[17];
						deliveredAs = row[18];
						eshopRank = row[19];
						analysisDate = row[21];
					} else if (row.length == 28) {
						dateOfLastSale = row[17];
						dateOfLastDelivery = row[18];
						releaseDate = row[19];
						deliveredAs = row[20];
						eshopRank = row[21];
						analysisDate = row[23];
					}

					ArticleRow article = new ArticleRow();
					article.setRank(rank);
					article.setFirstCode(firstCode);
					article.setEan(ean);
					article.setName(name);
					article.setSales(sales);
					article.setSales2(sales2);
					article.setRevenue(revenue);
					article.setStoredAmount(storedAmount);
					article.setSupply(supplies);
					article.setLocations(locations);
					article.setPrice(price);
					article.setDph(dph);
					article.setSupplier(supplier);
					article.setAuthor(author);
					article.setDateOfLastSale(dateOfLastSale);
					article.setDateOfLastDelivery(dateOfLastDelivery);
					article.setRealeaseDate(releaseDate);
					article.setDeliveredAs(deliveredAs);
					article.setEshopRank(eshopRank);
					article.setAnalysisDate(analysisDate);
					System.out.println(ean);
					map.put(ean, article);

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

	private HashMap<String, ArticleRow> readSSBAnalysis(File f) {
		HashMap<String, ArticleRow> map = new HashMap<>();
		Workbook wb;

		try {
			wb = WorkbookFactory.create(f);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));

			for (Row row : sheet) {
				if (row.getCell(1) == null) {
					break;
				}

				String ean = getValue(row, 0);
				String name = getValue(row, 1);
				String soldAmount = getValue(row, 2);
				String storedAmount = getValue(row, 3);
				String supply = getValue(row, 4);
				String supplier = getValue(row, 5);
				String revenue = getValue(row, 6);
				String price = getValue(row, 7);
				String loc = getValue(row, 8);
				// String dateOflastSale = getValue(row, 9);
				// String dateOfDelivery = getValue(row, 10);
				String dateOflastSale = getDateValue(row, 9);
				String dateOfDelivery = getDateValue(row, 10);
				String author = getValue(row, 12);

				String deliveredAs = "Komise";
				if (supplier.contains("__P")) {
					deliveredAs = "Pevno";
				}

				ArticleRow article = new ArticleRow();

				article.setEan(ean);
				article.setName(name);
				article.setSales(soldAmount.substring(0, soldAmount.indexOf(".")));
				article.setRevenue(revenue);
				article.setStoredAmount(storedAmount);
				article.setSupply(supply);
				article.setLocations(loc);
				article.setPrice(price);
				if (supplier.contains("_P")) {
					supplier = supplier.replace("_P", "");
					supplier = supplier.replaceAll("_", "");
				} else {
					supplier = supplier.replace("_K", "");
					supplier = supplier.replaceAll("_", "");
				}
				article.setSupplier(supplier);
				article.setAuthor(author);

				article.setDateOfLastSale(dateOflastSale);
				article.setDateOfLastDelivery(dateOfDelivery);
				article.setDeliveredAs(deliveredAs);
				map.put(ean, article);
			}

			wb.close();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		createOfflineFile(map);
		return map;
	}

	private void createOfflineFile(HashMap<String, ArticleRow> analysis) {
		Message message = new Message();
		System.out.println(analysis.size());
		message.createAnalysis(analysis);
		try {
			FileOutputStream fout = new FileOutputStream(
					AnalysisModel.getInstance().getSettings().getPath() + "/" + "ssbAnalyza");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(message);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getDateValue(Row row, int cellNumber) {
		String value = "";
		try {
			Date date = row.getCell(cellNumber).getDateCellValue();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			value = formatter.format(date);
		} catch (NullPointerException e) {
			return value;
		}
		return value;
	}

	private String getValue(Row row, int cellNumber) {
		String value = "";
		try {
			value = row.getCell(cellNumber).toString();
		} catch (NullPointerException e) {
			return value;
		}
		return value;
	}

	private String cleanString(String s) {
		return s.replaceAll("\"", "").replaceAll("&quot;", "");
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
