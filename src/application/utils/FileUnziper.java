package application.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUnziper {

	/*
	 * This method unzips files so that they can be renamed and moved via the
	 * "changeAllFiles" method.
	 */

	public static void unzip(File source, File destination) {
		byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(source); ZipInputStream zis = new ZipInputStream(fis)) {
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				try (FileOutputStream fos = new FileOutputStream(destination)) {
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					zis.closeEntry();
					ze = zis.getNextEntry();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}