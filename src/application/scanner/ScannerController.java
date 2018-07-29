package application.scanner;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScannerController {

	private ArrayList<Article> articles = ScannerModel.getInstance().getArticles();
	private ObservableList<Article> dataForList = FXCollections.observableArrayList();

	@FXML
	private AnchorPane root;

	@FXML
	private TextField input;

	@FXML
	private Label totalAmountOfBooks;
	@FXML
	private Label totalAmountOfRows;

	@FXML
	private Button removeButton;
	@FXML
	private Button removeAllButton;
	@FXML
	private Button updateButton;
	@FXML
	private TableView<Article> table;
	@FXML
	private TableColumn<Article, String> amountColumn;
	@FXML
	private TableColumn<Article, String> eanColumn;
	@FXML
	private Label infoLabel;
	@FXML
	private Label serverInfoLabel;
	@FXML
	private Label ipLabel;
	@FXML
	private JFXButton exportButton;

	@FXML
	private JFXButton settingsButton;

	@FXML
	public void initialize() {
		setupTable();
		handleEditingOfAmountColumn();
		refreshTableData();
		Server.getInstance().waitForResponse(serverInfoLabel, articles, dataForList);
		ipLabel.setText("IP adresa tohoto počítače " + getIp());
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

	private void setupTable() {
		table.setPlaceholder(new Label(""));
		table.setEditable(true);
	}

	private void refreshTableData() {
		dataForList.removeAll(dataForList);
		dataForList.addAll(articles);
		table.setItems(dataForList);
		table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ean"));
		table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));
		updateInfo();
		InfoModel.getInstance().updateInfo("");
	}

	private void handleEditingOfAmountColumn() {
		amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		amountColumn.setOnEditCommit(event -> {
			int oldValue = Integer.parseInt(event.getOldValue());
			if (!isNegative(oldValue, Integer.parseInt(event.getNewValue()))) {
				((Article) event.getTableView().getItems().get(event.getTablePosition().getRow()))
						.editAmountValue(event.getNewValue());
			}
			refreshTableData();
		});
	}

	private boolean isNegative(int oldValue, int newValue) {
		return (oldValue + newValue) < 0;
	}

	private void updateInfo() {
		totalAmountOfBooks.setText(calculateAmount() + "");
		totalAmountOfRows.setText(dataForList.size() + "");
	}

	private int calculateAmount() {
		int amount = 0;
		for (Article a : articles) {
			amount += Integer.parseInt(a.getAmount());
		}
		return amount;
	}

	@FXML
	public void onEnter(ActionEvent ae) {
		addEan();
		refreshTableData();
	}

	private void addEan() {
		String ean = input.getText();
		if (!ean.isEmpty()) {
			handleAddingEan(ean);
		}
		input.clear();
	}

	private void handleAddingEan(String ean) {
		if (articles.isEmpty()) {
			articles.add(0, new Article(ean));
		} else {
			iterateList(articles, ean);
		}
		input.setText("");
	}

	private void iterateList(ArrayList<Article> articles, String ean) {
		boolean containsEan = false;
		for (int i = 0; i < articles.size(); i++) {
			Article a = articles.get(i);
			containsEan = false;
			if (ean.equalsIgnoreCase(a.getEan())) {
				incrementAmount(a);
				containsEan = true;
				break;
			}
		}
		if (!containsEan) {
			articles.add(0, new Article(ean));
		}
	}

	private void incrementAmount(Article a) {
		int amount = Integer.parseInt(a.getAmount()) + 1;
		String currentEan = a.getEan();
		articles.remove(a);
		articles.add(0, new Article(currentEan));
		articles.get(0).editAmountValue(amount + "");
	}

	@FXML
	void handleExportButton(ActionEvent event) {
		if (table.getItems().size() > 0) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					ExcelUtils excel = new ExcelUtils();
					excel.writeFileTwoInputs(table.getItems(), ScannerModel.getInstance().getSettings().getPath(),
							"vratka.xlsx");
				}
			});
			t1.start();
			InfoModel.getInstance().updateInfo("Soubor byl exportován.");
		} else {
			InfoModel.getInstance().updateInfo("\t\tVratka je prázdná.");
		}

	}

	@FXML
	void handleUpdateButton(ActionEvent event) {
		updateInfo();
	}

	@FXML
	void handleRemoveButton() {
		Article selectedArticle = table.getSelectionModel().getSelectedItem();
		try {
			articles.remove(selectedArticle);
		} catch (NullPointerException e) {
			System.out.println("No item is selected");
		}
		refreshTableData();
	}

	@FXML
	void handleRemoveAllButton(ActionEvent event) {
		articles.clear();
		refreshTableData();
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("ScannerSettings.fxml"));
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
