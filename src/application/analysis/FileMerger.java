package application.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import application.shared.ArticleRow;

public class FileMerger {

	private File dataFile = new File(
			System.getProperty("user.dir") + File.separator + "data" + File.separator + "data.xlsx");

	private HashMap<String, ArticleRow> data;
	private HashMap<String, ArticleRow> analysis;
	private HashMap<String, ArticleRow> newBooks = new HashMap<>();

	public FileMerger(File analysisFile) {
		analysis = AnalysisModel.getInstance().getAnalysis();
		data = AnalysisModel.getInstance().getData();
	}

	public void startMergingFiles() {
		createMapOfDifferences();
		if (!newBooks.isEmpty()) {
			mergeFiles();
		}
	}

	private void createMapOfDifferences() {
		for (String s : analysis.keySet()) {
			if (data.get(s) == null) {
				newBooks.put(s, analysis.get(s));
			}
		}
	}

	private void mergeFiles() {
		Workbook workbook;
		try {
			FileInputStream fileInputStream = new FileInputStream(dataFile);
			workbook = WorkbookFactory.create(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();

			for (String s : newBooks.keySet()) {
				ArticleRow a = newBooks.get(s);
				String ean = a.getEan();
				String name = a.getName();
				rowCount++;
				Row row = sheet.createRow(rowCount);
				row.createCell(0);
				row.createCell(1);
				sheet.getRow(rowCount).getCell(0).setCellValue(ean);
				sheet.getRow(rowCount).getCell(1).setCellValue(name);
			}
			FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
			workbook.write(fileOutputStream);
			workbook.close();
			fileOutputStream.close();

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
