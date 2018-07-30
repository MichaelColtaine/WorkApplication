package application.kosmas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import application.RowRecord;

public class KosmasFileMover {

	private String fromDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	private String toDirectory;
	private ArrayList<RowRecord> fileNames = new ArrayList<>();

	public KosmasFileMover() {

	}

	public void move() throws IOException {
		File from = new File(fromDirectory);
		for (File f : from.listFiles()) {
			File newFile = new File(toDirectory + renameFile(f.getName()));
			if (newFile.exists()) {
				newFile.delete();
			}
			f.renameTo(newFile);
		}
	}

	private String renameFile(String fullName) {
		StringBuilder sb = new StringBuilder();
		String deliveryNoteName = fullName.toUpperCase().replace(".TXT", "");
		fullName = fullName.toLowerCase().replace("-", "");
		if (fullName.contains("f")) {
			sb.append(fullName.substring(6));
		} else if (fullName.contains("k")) {
			sb.append(fullName.substring(6));
		} else {
			sb.append(fullName.substring(5));
		}
		String temp = sb.toString();
		if (temp.length() > 7) {
			sb.delete(0, sb.toString().length());
			sb.append(temp.substring(temp.length() - 7));
		}
		fileNames.add(new RowRecord(deliveryNoteName, sb.toString(), "-38,00%"));
		return sb.toString();
	}

	public void setDestinantionDirectory(String toDirectory) {
		this.toDirectory = toDirectory + File.separator;
	}

	public ArrayList<RowRecord> getListOfNames() {
		return fileNames;
	}

}
