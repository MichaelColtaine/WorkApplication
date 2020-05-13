package application.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jfoenix.controls.JFXButton;
import com.mtr.application.shared.ArticleRow;
import com.mtr.application.shared.Message;
import com.opencsv.CSVReader;

import application.Model;
import application.infobar.InfoModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
	private JFXButton uploadFtpButton;

	@FXML
	private Label databaseLabel;

	@FXML
	private Label errorLabel;

	@FXML
	private ProgressIndicator progressBar;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	public void initialize() {
		ipLabel.setText("IP adresa tohoto poèítaèe: " + getIp());
		AnalysisSender.getInstance().listen(serverInfoLabel, progressBar);
		ExportReceiver.getInstance().listen(databaseLabel);
		System.out.println(files.size());
		setupComboboxAndTableview();
	}

	private void setupComboboxAndTableview() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("Praha - Václavské Námìstí", "Praha - Arkády Pankrác", "Praha - Andìl City",
				"Praha - OD Kotva", "Praha - NC Metrolope Zlièín", "Praha - OC Letòany", "Praha - Dejvice",
				"Praha - Vivo! Hostivaø", "Praha - NC Eden", "Brno - Joštova", "Brno - Galerie Vaòkovka",
				"Ostrava - Forum Nová Karolína", "Ostrava - OC Galerie", "Èeské Budìjovice - IGY Centrum",
				"Èeské Budìjovice - OD Prior", "Hradec Králové - OC Fontána", "Karviná - Tesco", "Kolín - OC Futurum",
				"Liberec - OC Nisa", "Liberec - NG Plaza", "Most - Central", "Olomouc - Galerie Šantovka",
				"Olomouc - City", "Opava - OC Silesia", "Pardubice- Atrium Palác", "Plzeò - Námìstí Republiky",
				"Plzeò - Sedláèkova", "Plzeò - OC Plzeò", "Teplice - Galerie Teplice", "Tachov - Námìstí Republiky",
				"Ústí nad Labem - Forum");

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
					InfoModel.getInstance().updateInfo("Soubor byl naèten");
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

	private HashMap<String, ArticleRow> readAnalysisFile(File f) {
		HashMap<String, ArticleRow> map = new HashMap<>();

		HashMap<String, ArrayList<String>> suppliers = new HashMap<>();
		try {
			InputStream is = new FileInputStream(f);
			@SuppressWarnings("deprecation")

			CSVReader reader = new CSVReader(new InputStreamReader(is), ';', '\"', 1);
			String[] record;
			int rankingCounter = 0;
			while ((record = reader.readNext()) != null) {

				// System.out.println(record.length);
				if (record.length == 1) {

				} else {
					rankingCounter++;
					String ranking = rankingCounter + "";
					String eshopCode = record[1];
					String ean = record[2];

					String name = record[3];
					if (name.contains("Dárková knižní") || name.contains("Klubový slevový poukaz")
							|| name.contains("VOUCHER")) {
						rankingCounter--;
						continue;
					}
					String sales1 = record[4];
					String sales2 = record[5];
					String revenue = record[6];
					String stored = record[7];
					String daysOfSupplies = record[8];
					String location = record[9];
					String price = record[10];
					String supplier = record[12];
					String author = record[13];

					String dontOrder = record[14];

					String dateOfLastSale = record[15];
					String dateOfLastDelivery = record[16];
					String releaseDate = record[17];
					String commision = record[18];
					String rankingEshop = record[19];
					String sales1DateSince = record[20];
					String sales1DateTo = record[21];
					String sales1Days = record[22];
					String sales2DateSince = record[23];
					String sales2DateTo = record[24];
					String sales2Days = record[25];

					System.out.println(dateOfLastSale + " " + dateOfLastDelivery);

					if (!suppliers.containsKey(supplier)) {
						ArrayList<String> list = new ArrayList<>();
						list.add(revenue);
						suppliers.put(supplier, list);
					} else {

						suppliers.get(supplier).add(revenue);
					}

					ArticleRow shared = new ArticleRow();

					shared.setFirstCode(eshopCode);
					shared.setRank(ranking);
					shared.setEan(ean);
					shared.setName(name);
					shared.setSales(sales1);
					shared.setSales2(sales2);
					shared.setRevenue(revenue);
					shared.setStoredAmount(stored);
					shared.setSupply(daysOfSupplies);
					shared.setLocations(location);
					shared.setPrice(price);
					shared.setSupplier(supplier);
					shared.setAuthor(author);
					shared.setDateOfLastSale(dateOfLastSale);
					shared.setDateOfLastDelivery(dateOfLastDelivery);
					shared.setRealeaseDate(releaseDate);
					shared.setDeliveredAs(commision);
					shared.setEshopRank(rankingEshop);
//					shared.setSales1Date(sales1DateSince);
//					shared.setSales1DateTo(sales1DateTo);
//					shared.setSales1Days(sales1Days);
//					shared.setSales2DateSince(sales2DateSince);
//					shared.setSales2DateTo(sales2DateTo);
//					shared.setSales2Days(sales2Days);
					shared.setDontOrder(dontOrder);
					map.put(ean, shared);
				}

			}
			System.out.println(map.size() + " polozek v analyze");
			reader.close();

			for (String supplier : suppliers.keySet()) {
				double total = 0;

				for (String s : suppliers.get(supplier)) {
					s = s.replace(",", ".");
					double revenue = Double.parseDouble(s);
					total += revenue;
				}

				// System.out.println(supplier + " " + total);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return map;
	}
//	
//	
//	boolean isFirstLine = true;
//	private HashMap<String, ArticleRow> readAnalysisFile(File f) {
//		HashMap<String, ArticleRow> map = new HashMap<>();
//		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
//			String line = "";
//
//			while ((line = br.readLine()) != null) {
//				if (isFirstLine) {
//					isFirstLine = false;
//					continue;
//				}
//				try {
//
//					line = cleanString(line);
//
//					String[] row = line.split(";");
//					String rank = row[0];
//					String firstCode = row[1];
//					String ean = row[2];
//					String name = row[3];
//					String sales = row[4];
//					String sales2 = row[5];
//					String revenue = row[6];
//					String storedAmount = row[7];
//					String supplies = row[8];
//					String locations = row[9];
//					String price = row[10];
//					String dph = row[11];
//					String supplier = row[12];
//					String author = row[13];
//					String dateOfLastSale = row[14];
//					String dateOfLastDelivery = row[15];
//					String releaseDate = row[16];
//					String deliveredAs = row[17];
//					String eshopRank = row[18];
//					String analysisDate = row[20];
//
//					if (row.length != 25) {
//						System.out.println(row.length + " " + ean);
//					}
//
//					if (row.length == 27) {
//						dateOfLastSale = row[16];
//						dateOfLastDelivery = row[17];
//						releaseDate = row[18];
//						deliveredAs = row[19];
//						eshopRank = row[20];
//						analysisDate = row[22];
//
//					} else if (row.length == 26) {
//						dateOfLastSale = row[15];
//						dateOfLastDelivery = row[16];
//						releaseDate = row[17];
//						deliveredAs = row[18];
//						eshopRank = row[19];
//						analysisDate = row[21];
//					} else if (row.length == 28) {
//						dateOfLastSale = row[17];
//						dateOfLastDelivery = row[18];
//						releaseDate = row[19];
//						deliveredAs = row[20];
//						eshopRank = row[21];
//						analysisDate = row[23];
//					}
//
//					ArticleRow article = new ArticleRow();
//					article.setRank(rank);
//					article.setFirstCode(firstCode);
//					article.setEan(ean);
//					article.setName(name);
//					article.setSales(sales);
//					article.setSales2(sales2);
//					article.setRevenue(revenue);
//					article.setStoredAmount(storedAmount);
//					article.setSupply(supplies);
//					article.setLocations(locations);
//					article.setPrice(price);
//					article.setDph(dph);
//					article.setSupplier(supplier);
//					article.setAuthor(author);
//					article.setDateOfLastSale(dateOfLastSale);
//					article.setDateOfLastDelivery(dateOfLastDelivery);
//					article.setRealeaseDate(releaseDate);
//					article.setDeliveredAs(deliveredAs);
//					article.setEshopRank(eshopRank);
//					article.setAnalysisDate(analysisDate);
//					System.out.println(ean);
//					map.put(ean, article);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					continue;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return map;
//	}

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
			System.out.println(files.isEmpty() + " test " + (files != null));
			files.clear();
			fileNameLabel.setText("");
		}
	}

	private Properties properties;
	private String password = "", username = "", address = "";

	@FXML
	private void handleUploadFtpButtonAction(ActionEvent event) {
		errorLabel.setText("");
		if (files == null || files.isEmpty()) {
			errorLabel.setText("Musíte vložit soubor s analýzou.");
		} else if (comboBox.getSelectionModel().getSelectedItem() == null) {
			errorLabel.setText("Musíte vybrat prodejnu!");
		} else {

			new Thread(new Runnable() {

				@Override
				public void run() {
					InfoModel.getInstance().updateInfo("");
					progressBar.setVisible(true);
					FTPClient client = new FTPClient();

					readConfigFile();
					try {
						client.connect(properties.getProperty("address"));
						boolean login = client.login(properties.getProperty("username"),
								properties.getProperty("password"));
						if (login) {
							InfoModel.getInstance().updateInfo("Pøihlašeno na FTP server.");
							String selectedDirectory = Normalizer.normalize(
									comboBox.getSelectionModel().getSelectedItem().toLowerCase(), Normalizer.Form.NFD);
							selectedDirectory = selectedDirectory.replaceAll("[^\\p{ASCII}]", "");

							String directory = "/prodejny/" + selectedDirectory;
							// client.changeWorkingDirectory("prodejny");
							client.makeDirectory(directory);
							client.changeWorkingDirectory(directory);
							client.storeFile("analyza.csv", new FileInputStream(files.get(0)));
							InfoModel.getInstance().updateInfo("Data byla nahrána na FTP.");
						} else {
							InfoModel.getInstance().updateInfo("Nepodaøilo se pøihlásit na FTP server.");
						}

						client.logout();
						client.disconnect();
						progressBar.setVisible(false);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}

			}).start();

		}

	}

	private void readConfigFile() {

		InputStream input;
		try {

//			input = new FileInputStream(getClass().getClassLoader().getResourceAsStream("config/config"));
			properties = System.getProperties();
			properties.load(getClass().getClassLoader().getResourceAsStream("config/config"));
			username = properties.getProperty("username");
			password = properties.getProperty("password");
			address = properties.getProperty("address");
		} catch (IOException e) {
			progressBar.setVisible(false);
			InfoModel.getInstance().updateInfo("Nenalezen config soubor.");
			e.printStackTrace();
		}

		// try (FileReader reader = new FileReader("src/config")) {
		// properties.load(reader);
		// username = properties.getProperty("username");
		// password = properties.getProperty("password");
		// address = properties.getProperty("address");
		//
		// } catch (FileNotFoundException e) {
		// progressBar.setVisible(false);
		// InfoModel.getInstance().updateInfo("Nenalezen config soubor.");
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
