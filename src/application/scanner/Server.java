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
	static Socket s;
	static InputStreamReader is;
	static BufferedReader br;
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

	// public void waitForResponse(Label label, ArrayList<Article> articles,
	// ObservableList<Article> dataForList) {
	// final Task<Void> task = new Task<Void>() {
	// @Override
	// protected Void call() {
	// try {
	// ss = new ServerSocket(port);
	// while (true) {
	// System.out.println("Server is waiting for response");
	// changeLabel(label, "Připojení se zdařilo.");
	// s = ss.accept();
	// System.out.println("Connected");
	// is = new InputStreamReader(s.getInputStream());
	// br = new BufferedReader(is);
	// text = br.readLine();
	// serverArticles = convertStringToList(text);
	// createTable(articles, dataForList);
	// }
	// } catch (BindException e) {
	// changeLabel(label, "Připojení selhalo, port už je používán. Změň číslo
	// postu.");
	// System.out.println(e.getMessage() + " Thrown by " +
	// e.getClass().getSimpleName());
	// } catch (IOException e) {
	// System.out.println(e.getMessage() + " Thrown by " +
	// e.getClass().getSimpleName());
	// }
	// return null;
	// }
	// };
	// new Thread(task).start();
	// }

	public void waitForResponse(Label label, ArrayList<Article> articles, ObservableList<Article> dataForList) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					ss = new ServerSocket(port);
					ss.setReuseAddress(true);

					while (true) {
						System.out.println("Server is waiting for response");
						changeLabel(label, "Připojení se zdařilo.");
						s = ss.accept();
						System.out.println("Connected");
						is = new InputStreamReader(s.getInputStream());
						br = new BufferedReader(is);
						text = br.readLine();
						serverArticles = convertStringToList(text);
						createTable(articles, dataForList);
					}
				} catch (BindException e) {
					changeLabel(label, "Připojení selhalo, port už je používán.");
					System.out.println(e.getMessage() + " Thrown by " + e.getClass().getSimpleName());
				} catch (IOException e) {
					System.out.println(e.getMessage() + " Thrown by " + e.getClass().getSimpleName());
				}
				return null;
			}
		};
		new Thread(task).start();
	}

	public static void closeAll() throws IOException {
		if (s != null) {
			s.close();
		}
		if (ss != null) {
			ss.close();
		}
		if (br != null) {
			br.close();
		}
		if (is != null) {
			is.close();
		}

	}

	private void createTable(ArrayList<Article> articles, ObservableList<Article> dataForList) {
		articles.clear();
		dataForList.clear();
		for (ServerArticle a : serverArticles) {
			Article article = new Article(a.getEan());
			article.editAmountValue(a.getAmount());
			articles.add(article);

		}
		dataForList.addAll(articles);
	}

	private void changeLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

	public String getText() {
		return this.text;
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

		for (ServerArticle a : articles) {
			System.out.println(a.getEan() + " " + a.getAmount());
		}
		return articles;
	}

}
