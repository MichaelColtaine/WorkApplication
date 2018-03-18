package application.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javafx.scene.control.Label;

public class FileChanger {

	private static String FROM_DIRECTORY = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	public static List<Label> labels = new ArrayList<>();

	public FileChanger() {

	}

	/*
	 * This method unzips files so that they can be renamed and moved via the
	 * "changeAllFiles" method.
	 */

	public static void unzip(File source, File destination) throws IOException {
		byte[] buffer = new byte[1024];
		FileInputStream fis = new FileInputStream(source);
		ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry ze = zis.getNextEntry();
		while (ze != null) {
			FileOutputStream fos = new FileOutputStream(destination);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zis.closeEntry();
			ze = zis.getNextEntry();
		}
		fis.close();

	}

	/*
	 * This method shortens file names and and moves them as .TXT file to a
	 * directory that it takes as an argument. For example bob123 -> 123.txt
	 */
	public static void changeAllEuroFiles(String toDirectory) {
		File directory = new File(FROM_DIRECTORY);
		StringBuilder sb = new StringBuilder();
		for (File source : directory.listFiles()) {
			sb.delete(0, sb.length());
			labels.add(new Label(source.getName().substring(11, 14)));
			sb.append(toDirectory).append(File.separator).append(source.getName().substring(11, 14)).append(".txt");
			File destination = new File(sb.toString());
			try {
				unzip(source, destination);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public static void changeAllKosmasFiles(String toDirectory) {
		File directory = new File(FROM_DIRECTORY);
		StringBuilder sb = new StringBuilder();
		FileOutputStream fos;
		for (File source : directory.listFiles()) {
			sb.delete(0, sb.length());

			sb.append(toDirectory).append(File.separator).append(source.getName());
			File destination = new File(sb.toString());
			try {
				fos = new FileOutputStream(destination);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}