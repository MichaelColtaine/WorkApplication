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
		//read();
		read2();
		convertData();
		write(name);
	}

	public void read2(){
		String line = "";
		lines.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			while ((line = br.readLine()) != null) {
				String ean = findEan(line);
				String shortenedString = shortenEan(ean);

				if(shortenedString.length() > 0){
					StringBuilder sb = new StringBuilder();
					if(ean.length() == 14){
						sb.append(shortenedString);
						sb.append("  ");
						line =line.replace(ean, sb.toString());
					} else if(ean.length() == 13){
						sb.append(shortenedString);
						sb.append(" ");
						line = line.replace(ean, sb.toString());
					}
				}
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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


	public  String findEan(String line){
		int startIndex = 124;
		int endIndex = 138;
		return line.substring( startIndex, endIndex );
	}

	public  int getEanLength(String input){
		int count = 0;
		for(int i =0;  i < input.length(); i++){
			if(input.charAt(i) != ' '){
				count++;
			}
		}
		return count;
	}

	public  String shortenEan(String ean){
		String shortenedString = null;
		int eanLength = getEanLength(ean);
		if(eanLength < 13){
			shortenedString = "";
		} else if(eanLength == 14) {
			shortenedString = ean.substring(2, ean.length());
		} else  if (eanLength == 13){
			shortenedString = ean.substring(2, ean.length());
		}
		return shortenedString;
	}

}
