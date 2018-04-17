package application.albatros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class FileChanger {
	File input = new File("input.txt");
	File output = new File("out.txt");
	ArrayList<String> lines = new ArrayList<>();
	ArrayList<String> newLines = new ArrayList<>();

	public FileChanger() {

	}

	public void read() {
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(lines.size());
	}

	public void convertData() {
		String first, last, newLine;
		for (String line : lines) {
			first = line.substring(0, 13);
			last = line.substring(124, 137);
			newLine = line;
			newLine = newLine.replace(last, first);
			newLine = newLine.replaceFirst(first, last);
			newLines.add(newLine);

		}
	}

	public void write() {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(output))) {
			for (String line : newLines) {
				wr.write(line);
				wr.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
