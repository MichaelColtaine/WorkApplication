package application.kosmas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KosmasFileMover {

	private String fromDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	private String toDirectory;
	private ArrayList<String> fileNames = new ArrayList<>();

	public KosmasFileMover() {

	}

	public void move() throws IOException {
		File from = new File(fromDirectory);
		for (File f : from.listFiles()) {
			System.out.println(toDirectory);
			File newFile = new File(toDirectory + renameFile(f.getName()));
			if (newFile.exists()) {
				newFile.delete();
			}
			f.renameTo(newFile);
		}
	}

	private String renameFile(String fullName) {
		StringBuilder sb = new StringBuilder();
		fullName = fullName.toLowerCase().replace("-", "");
		if (fullName.contains("f")) {
			sb.append(fullName.substring(6));
		} else if (fullName.contains("k")) {
			sb.append(fullName.substring(6));
		} else {
			sb.append(fullName.substring(5));
		}
		fileNames.add(fullName.toUpperCase().replaceAll(".TXT", " ->  ") + " " + sb.toString() + ", RABAT: -38");
		return sb.toString();
	}

	public void setDestinantionDirectory(String toDirectory) {
		this.toDirectory = toDirectory + File.separator;
	}

	public ArrayList<String> getListOfNames() {
		return fileNames;
	}

}
