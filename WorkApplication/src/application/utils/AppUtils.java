package application.utils;

import java.io.File;

import javafx.scene.control.TextField;

public class AppUtils {

	public static boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	public static boolean isInvalidInput(TextField input) {
		return input.getText().isEmpty();
	}
	
	public static boolean isInvalidLength(TextField input) {
		return input.getText().length() > 5;
	}

}
