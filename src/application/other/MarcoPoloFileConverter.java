package application.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.utils.ExcelRecord;

public class MarcoPoloFileConverter {
	private File fromDirectory, toDirectory;

	public void convertMarcoDeliviryNoteToExcel(String fromPath, String toPath) {
		createDirectoriesIfDontExist(fromPath, toPath);
		for (File f : fromDirectory.listFiles()) {
			writeFile(readMarcoFile(f), toPath, f.getName());
			f.delete();
		}
	}

	private void createDirectoriesIfDontExist(String fromDirectoryPath, String toDirectoryPath) {
		fromDirectory = new File(fromDirectoryPath);
		toDirectory = new File(toDirectoryPath);
		if (!fromDirectory.exists()) {
			fromDirectory.mkdirs();
		}

		if (!toDirectory.exists()) {
			toDirectory.mkdirs();
		}
	}

	private void writeFile(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		createRowAndAddData(records, sheet);
		resizeColumns(records, sheet);
		convertTXTFileToExcel(workbook, toDirectoryPath, fileName);

	}

	private void createRowAndAddData(List<ExcelRecord> records, Sheet sheet) {
		int i = 0;
		for (ExcelRecord r : records) {
			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue(r.getEan());
			row.createCell(1).setCellValue(r.getAmount());
			row.createCell(3).setCellValue(r.getPrice());
			i++;
		}
	}

	private void resizeColumns(List<ExcelRecord> records, Sheet sheet) {
		for (int k = 0; k < records.size(); k++) {
			sheet.autoSizeColumn(k);
		}
	}

	private void convertTXTFileToExcel(Workbook workbook, String toDirectoryPath, String fileName) {
		try {

			FileOutputStream fileOut = new FileOutputStream(
					toDirectoryPath + File.separator + fileName.toLowerCase().replace(".txt", ".xlsx"));

			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ExcelRecord> readMarcoFile(File file) {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splittedLine = line.split(",");
				String ean = splittedLine[0];
				Collections.reverse(Arrays.asList(splittedLine));
				String amount = splittedLine[4];
				String price = splittedLine[3];
				records.add(new ExcelRecord(ean.replaceAll("\"", ""), amount.replaceAll(".0000", ""), price.replaceAll(".0000", "")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

}
