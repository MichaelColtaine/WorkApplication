package application.albatros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AlbatrosFileChanger {

	File input;
	String outputDirectory;
	ArrayList<String> lines = new ArrayList<>();
	ArrayList<String> newLines = new ArrayList<>();

	public AlbatrosFileChanger() {

	}

	public void setOuputDirectory(String path) {
		this.outputDirectory = path + File.separator;

	}

	public void changeFile(File f, String name) {
		input = f;
		read();
		convertData();
		write(name);
	}

	public void read() {
		String line = "";
		lines.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertData() {
		String first, last, newLine;
		newLines.clear();
		for (String line : lines) {
			first = line.substring(0, 13);
			last = line.substring(124, 137);
			newLine = line;
			newLine = newLine.replace(last, first);
			newLine = newLine.replaceFirst(first, last);
			newLines.add(newLine);

		}
	}

	public void write(String name) {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(outputDirectory + name))) {
			for (String line : newLines) {
				wr.write(line);
				wr.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
