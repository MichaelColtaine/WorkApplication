package application.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import application.Model;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class DataSender {

	static ServerSocket serverSocket;
	static Socket socket;

	int port = 9998;

	private DataSender() {

	}

	private static class LazyHolder {
		static final DataSender INSTANCE = new DataSender();
	}

	public static DataSender getInstance() {
		return LazyHolder.INSTANCE;
	}

	// tento
	public void listen(Label label) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					while (true) {
						changeLabel(label, "Server Online\nNaslouchá k portu èíslo: " + port);
						socket = serverSocket.accept();
						System.out.println("Connected");
						try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
							out.write(AnalysisModel.getInstance().getData());
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				} catch (BindException e) {
					changeLabel(label, "Server Offline\nPøipojení selhalo, port je již používán.");
					System.out.println(e.getMessage() + " 1Thrown by sender " + e.getClass().getSimpleName());
				} catch (IOException e) {
					changeLabel(label, "IO exception.");
					System.out.println(e.getMessage() + " 2Thrown by sender " + e.getClass().getSimpleName());
					// e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(task).start();
	}

	public static void closeAll() throws IOException {

		if (serverSocket != null) {
			serverSocket.close();
		}

		if (socket != null) {
			socket.close();
		}

	}

	private void changeLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

}
