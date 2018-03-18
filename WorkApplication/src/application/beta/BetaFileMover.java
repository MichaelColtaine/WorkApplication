package application.beta;

import java.io.File;
import java.io.IOException;

public class BetaFileMover {

	private String fromDirectory, toDirectory;
	private File from;

	public BetaFileMover() {

	}

	public void setFromDirectory(String from) {
		this.fromDirectory = from;
	}

	public void setToDirectory(String to) {
		this.toDirectory = to;
	}

	public File getFromDirectory() {
		return this.from;
	}

	public void move() throws IOException {
		from = new File(fromDirectory);
		for (File f : from.listFiles()) {
			deletePdfFiles(f);
			File newFile = new File(toDirectory + renameFile(f.getName().replaceAll(".txt", "")));
			if (newFile.exists()) {
				System.out.println("File exists and is being removed and replaced");
				newFile.delete();
			}
			f.renameTo(newFile);
		}
	}

	private void deletePdfFiles(File f) {
		if (f.getName().contains(".pdf")) {
			f.delete();
			System.out.println(f.getName() + " deleted");
		}
	}

	private String renameFile(String fullName) {
		StringBuilder sb = new StringBuilder();
		sb.append(fullName.substring(fullName.length() - 3));
		return sb.append(".txt").toString();
	}

}