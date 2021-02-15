package application.analysis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.mtr.application.shared.ArticleRow;
import com.mtr.application.shared.Message;

import application.Model;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class ExportReceiver {

	static ServerSocket serverSocket;
	static Socket socket;

	int port = 5678;

	public ExportReceiver() {

	}

	private static class LazyHolder {
		static final ExportReceiver INSTANCE = new ExportReceiver();
	}

	public static ExportReceiver getInstance() {
		return LazyHolder.INSTANCE;
	}

	public void listen(Label label) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					System.out.println("Waiting for connection");
					while (true) {
						changeLabel(label, "Server Exportu Online\nNaslouchá k portu èíslo: " + port);
						Socket socket = serverSocket.accept();
						System.out.println("Connected");
						ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
						System.out.println(objectInputStream.readObject());
						Message inputMessage = (Message) objectInputStream.readObject();
						
						AnalysisModel.getInstance().setOrders(inputMessage.getOrders());
						AnalysisModel.getInstance().setReturns(inputMessage.getReturns());
						ExportFileCreator fileCreator = new ExportFileCreator();
						fileCreator.createFiles();
					}
				} catch (BindException e) {
					changeLabel(label, "Server Offline\nPøipojení selhalo, port je již používán.");
					System.out.println(e.getMessage() + " 1Thrown by sender " + e.getClass().getSimpleName());
				} catch (IOException e) {
					changeLabel(label, "IO exception.");
					System.out.println(e.getMessage() + " 2Thrown by sender " + e.getClass().getSimpleName());

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
