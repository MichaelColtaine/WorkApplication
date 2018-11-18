package application.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class ScannerServer {

	static ServerSocket serverSocket;
	static Socket socket;

	private String text, fileName;
	ArrayList<ServerArticle> listOfArticlesFromClient;
	int port = 8889;

	private ScannerServer() {

	}

	private static class LazyHolder {
		static final ScannerServer INSTANCE = new ScannerServer();
	}

	public static ScannerServer getInstance() {
		return LazyHolder.INSTANCE;
	}

	// tento
	public void listen(Label label, ArrayList<Article> articles, ObservableList<Article> dataForList, Label amountLabel,
			Label totalAmountOfBooks) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					while (true) {
						changeLabel(label, "Server Online\nNaslouchá k portu číslo: " + port);

						socket = serverSocket.accept();

						System.out.println("Connected");
						try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
							text = br.readLine();
							listOfArticlesFromClient = convertReceivingData(text);
							createTable(articles, dataForList, amountLabel, totalAmountOfBooks);
							changeLabel(label, "Konec spojení.");
						}

					}
				} catch (BindException e) {
					changeLabel(label, "Server Offline\nPřipojení selhalo, port je již používán.");
					System.out.println(e.getMessage() + " 1Thrown by " + e.getClass().getSimpleName());
				} catch (IOException e) {
					changeLabel(label, "IO exception.");
					System.out.println(e.getMessage() + " 2Thrown by " + e.getClass().getSimpleName());
					// e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(task).start();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ServerArticle> convertReceivingData(String json) {
		ArrayList<ServerArticle> list;
		HashMap<String, ArrayList<ServerArticle>> nameAndList;
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String, ArrayList<ServerArticle>>>() {
		}.getType();
		nameAndList = gson.fromJson(json, type);
		fileName = (String) nameAndList.keySet().toArray()[0] + ".xlsx";
		list = (ArrayList<ServerArticle>) nameAndList.values().toArray()[0];
		return list;
	}


	public static void closeAll() throws IOException {

		if (serverSocket != null) {
			serverSocket.close();
		}

		if (socket != null) {
			socket.close();
		}

	}

	private void createTable(ArrayList<Article> articles, ObservableList<Article> dataForList, Label amountLabel,
			Label totalAmountOfBooks) {
		articles.clear();
		dataForList.clear();
		int amount = 0;
		for (ServerArticle a : listOfArticlesFromClient) {
			Article article = new Article(a.getEan());
			article.editAmountValue(a.getAmount());
			articles.add(article);
			amount += Integer.parseInt(a.getAmount());
		}
		dataForList.addAll(articles);
		createFile(articles);
		updateAmountLabels(amountLabel, dataForList, totalAmountOfBooks, amount);
	}

	private void createFile(ArrayList<Article> articles) {
		ExcelUtils excel = new ExcelUtils();
		excel.writeFileTwoInputs(articles, ScannerModel.getInstance().getSettings().getPath(), fileName);
	}

	private void changeLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

	private void updateAmountLabels(Label amountLabel, ObservableList<Article> dataForList, Label totalAmountOfBooks,
			int amount) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				amountLabel.setText(dataForList.size() + "");
				totalAmountOfBooks.setText(amount + "");
			}
		});
	}

	public ArrayList<ServerArticle> getServerArticles() {
		return this.listOfArticlesFromClient;
	}

}
