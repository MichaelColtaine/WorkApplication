package application.analysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class DatabaseSender {

	static ServerSocket serverSocket;
	static Socket socket;

	int port = 9777;

	private DatabaseSender() {

	}

	private static class LazyHolder {
		static final DatabaseSender INSTANCE = new DatabaseSender();
	}

	public static DatabaseSender getInstance() {
		return LazyHolder.INSTANCE;
	}

	// tento
	public void listen() {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					while (true) {

						socket = serverSocket.accept();
						System.out.println("Connected");
						try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
							out.write(AnalysisModel.getInstance().getDatabase());
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				} catch (BindException e) {

					System.out.println(e.getMessage() + " 1Thrown by sender " + e.getClass().getSimpleName());
				} catch (IOException e) {

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

}
