package application.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class Server {

	static ServerSocket ss;
	static Socket socket;

	String text;
	ArrayList<ServerArticle> serverArticles;
	int port = 8889;

	private Server() {

	}

	private static class LazyHolder {
		static final Server INSTANCE = new Server();
	}

	public static Server getInstance() {
		return LazyHolder.INSTANCE;
	}

	// tento
	public void waitForResponse(Label label, ArrayList<Article> articles, ObservableList<Article> dataForList,
			Label amountLabel, Label totalAmountOfBooks) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					ss = new ServerSocket(port);
					ss.setReuseAddress(true);

					while (true) {
						System.out.println("Server is waiting for response");
						changeLabel(label, "Naslouchá k portu číslo: " + port);
						socket = ss.accept();
						System.out.println("Connected");
						try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
							text = br.readLine();
							serverArticles = convertStringToList(text);
							createTable(articles, dataForList, amountLabel, totalAmountOfBooks);
							changeLabel(label, "Konec spojení.");

						}
					}
				} catch (BindException e) {
					changeLabel(label, "Připojení selhalo, port už je používán.");
					System.out.println(e.getMessage() + " 1Thrown by " + e.getClass().getSimpleName());
				} catch (IOException e) {
					changeLabel(label, "IO exception.");
					System.out.println(e.getMessage() + " 2Thrown by " + e.getClass().getSimpleName());

				}
				return null;
			}
		};
		new Thread(task).start();
	}

	// public void waitForResponse(Label label, ArrayList<Article> articles,
	// ObservableList<Article> dataForList) {
	// final Task<Void> task = new Task<Void>() {
	// @Override
	// protected Void call() {
	// try {
	// ss = new ServerSocket(port);
	// ss.setReuseAddress(true);
	//
	// while (true) {
	// System.out.println("Server is waiting for response");
	// changeLabel(label, "Naslouchá k portu číslo: " + port);
	// socket = ss.accept();
	// System.out.println("Connected to client");
	// try (BufferedReader br = new BufferedReader(new
	// InputStreamReader(socket.getInputStream()));
	// PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
	// out.print("TEST");
	// out.flush();
	//// out.close();
	// text = br.readLine();
	// serverArticles = convertStringToList(text);
	// createTable(articles, dataForList);
	// changeLabel(label, "Konec spojení.");
	// }
	// }
	//
	// } catch (BindException e) {
	// changeLabel(label, "Připojení selhalo, port už je používán.");
	// System.out.println(e.getMessage() + " 1Thrown by " +
	// e.getClass().getSimpleName());
	//
	// } catch (IOException e) {
	// changeLabel(label, "IO exception.");
	// System.out.println(e.getMessage() + " 2Thrown by " +
	// e.getClass().getSimpleName());
	// e.printStackTrace();
	// }
	//
	// return null;
	// }
	// };
	// new Thread(task).start();
	// }

	public static void closeAll() throws IOException {

		if (ss != null) {
			ss.close();
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
		for (ServerArticle a : serverArticles) {
			Article article = new Article(a.getEan());
			article.editAmountValue(a.getAmount());
			articles.add(article);
			amount += Integer.parseInt(a.getAmount());
		}
		dataForList.addAll(articles);
		updateAmount(amountLabel, dataForList, totalAmountOfBooks, amount);
	}

	private void changeLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

	private void updateAmount(Label amountLabel, ObservableList<Article> dataForList, Label totalAmountOfBooks,
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
		return this.serverArticles;
	}

	public ArrayList<ServerArticle> convertStringToList(String txt) {
		ArrayList<ServerArticle> articles = new ArrayList<>();
		String[] rows = txt.split(";");
		for (String s : rows) {
			String[] temp = s.split("\\.");
			articles.add(new ServerArticle(temp[0], temp[1]));
		}
		return articles;
	}

}
