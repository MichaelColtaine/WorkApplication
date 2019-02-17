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
		System.out.println("Full name "+ fullName);
		
		if(fullName.contains("_20")) {
			int startIndex = fullName.indexOf("_");
			String wordToReplace = fullName.substring(startIndex, startIndex+5);
			fullName = fullName.replace(wordToReplace, "");
		}
		
		if (fullName.contains("f")) {
			sb.append(fullName.substring(5));
			// System.out.println(sb.toString());

		} else if (fullName.contains("k")) {
			sb.append(fullName.substring(5));
		} else {
			sb.append(fullName.substring(4));
		}
		String temp = sb.toString();

		if (temp.length() > 7) {
			System.out.println(temp + " " + temp.length());
			sb.delete(0, sb.toString().length());
			if (temp.length() == 8) {
				sb.append(temp.substring(temp.length() - 8)); // tady se mìni delka textu v listview
			} else {
				sb.append(temp.substring(temp.length() - 9));
			}
			System.out.println(sb.toString());
		} 
		
		System.out.println("temp " + temp.length() + " " + temp.toString());
		System.out.println("SB " + sb.length() + " " +sb.toString());
		fileNames.add(new RowRecord(deliveryNoteName, sb.toString(), ""));
		return sb.toString();
	}

	public void setDestinantionDirectory(String toDirectory) {
		this.toDirectory = toDirectory + File.separator;
	}

	public ArrayList<RowRecord> getListOfNames() {
		return fileNames;
	}

}
