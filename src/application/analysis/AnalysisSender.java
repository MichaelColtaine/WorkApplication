package application.analysis;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mtr.application.shared.ArticleRow;
import com.mtr.application.shared.Message;

import application.infobar.InfoModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class AnalysisSender {

	static ServerSocket serverSocket;
	static Socket socket;

	int port = 9998;

	public AnalysisSender() {

	}

	private static class LazyHolder {
		static final AnalysisSender INSTANCE = new AnalysisSender();
	}

	public static AnalysisSender getInstance() {
		return LazyHolder.INSTANCE;
	}

	public void listen(Label label, ProgressIndicator pb) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					System.out.println("Waiting for connection");
					while (true) {
						changeLabel(label, "Server Analýzy Online\nNaslouchá k portu èíslo: " + port);
						Socket socket = serverSocket.accept();
						System.out.println("Connected");
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						Message outputMessage = new Message();
						InfoModel.getInstance().updateInfo("Posílám data.");
						pb.setVisible(true);
						if (!AnalysisModel.getInstance().getAnalysis().isEmpty()) {
							InfoModel.getInstance().updateInfo("Posílám Analýzu...");
							outputMessage.createAnalysis(AnalysisModel.getInstance().getAnalysis());
						}
						objectOutputStream.writeObject(outputMessage);
						InfoModel.getInstance().updateInfo("Analýza odeslána");
						pb.setVisible(false);
					}
				} catch (BindException e) {
					changeLabel(label, "Server Offline\nPøipojení selhalo, port je již používán.");
					InfoModel.getInstance().updateInfo("BindException error");
					System.out.println(e.getMessage() + " 1Thrown by sender " + e.getClass().getSimpleName());
				} catch (IOException e) {
					InfoModel.getInstance().updateInfo("IOException errror");
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
