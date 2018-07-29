package application.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import application.euromedia.EuroModel;
import application.euromedia.EuromediaExcelConverter;

public class FileChanger {

	private static String TEMP_DIRECOTORY = System.getProperty("user.dir") + File.separator + "temp" + File.separator;

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
	public static void changeAllEuroFilesSSB(String toDirectory) {
		File directory = new File(TEMP_DIRECOTORY);
		StringBuilder sb = new StringBuilder();
		for (File source : directory.listFiles()) {
			sb.delete(0, sb.length());
			sb.append(toDirectory).append(File.separator)
					.append(source.getName().replaceAll(".zip", ".txt").substring(source.getName().length() - 7));
			File destination = new File(sb.toString());
			try {
				unzip(source, destination);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public static void changeAllEuroFilesFlores(String toDirectory) {
		File directory = new File(TEMP_DIRECOTORY);
		StringBuilder sb = new StringBuilder();
		for (File source : directory.listFiles()) {
			sb.delete(0, sb.length());
			sb.append(TEMP_DIRECOTORY).append(File.separator).append(source.getName().replaceAll("zip", "xls"));

			System.out.println(sb.toString());
			File destination = new File(sb.toString());

			try {
				unzip(source, destination);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		for (File f : directory.listFiles()) {
			if (f.getName().contains(".zip")) {
				f.delete();
			}
		}
		EuromediaExcelConverter converter = new EuromediaExcelConverter();
		converter.euromediaExcel(TEMP_DIRECOTORY, EuroModel.getInstance().getSettings().getPath());
		System.out.println("TEST3");
	}

}