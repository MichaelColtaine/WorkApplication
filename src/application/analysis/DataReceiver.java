package application.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.impl.io.SocketOutputBuffer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class DataReceiver {

	static ServerSocket serverSocket;
	static Socket socket;

	int port = 8799;

	private DataReceiver() {

	}

	private static class LazyHolder {
		static final DataReceiver INSTANCE = new DataReceiver();
	}

	public static DataReceiver getInstance() {
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

						try (BufferedReader br = new BufferedReader(
								new InputStreamReader(socket.getInputStream(), "CP1250"))) {
							String data = br.readLine();
							System.out.println(data);
							if (data.contains("objvr")) {
								data = data.replace("objvr", "");
								String[] temp = data.split("~o~");
								AnalysisModel.getInstance().setImportedData(data);
								AnalysisFileCreater s = new AnalysisFileCreater(data);
								s.createExcelReturnsFile(temp[0].split("~"));
								s.createExcelOrderFile(temp[1].split("~"));

							}

							if (data.contains("vr")) {
								data = data.replace("vr", "");
								System.out.println("test2");
								String[] temp = data.split("~o~");
								AnalysisModel.getInstance().setImportedData(data);
								AnalysisFileCreater s = new AnalysisFileCreater(data);
								s.createExcelReturnsFile(temp[0].split("~"));
							}

							if (data.contains("obj")) {
								data = data.replace("obj", "");
								System.out.println("test3");
								String[] temp = data.split("~o~");
								System.out.println(temp.length);
								AnalysisModel.getInstance().setImportedData(data);
								AnalysisFileCreater s = new AnalysisFileCreater(data);
								s.createExcelOrderFile(temp[1].split("~"));
							}

						}

					}
				} catch (BindException e) {
					System.out.println(e.getMessage() + " 1Thrown by receiver " + e.getClass().getSimpleName());
				} catch (IOException e) {
					System.out.println(e.getMessage() + " 2Thrown by receiver " + e.getClass().getSimpleName());
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
