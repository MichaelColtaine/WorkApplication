package application.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import application.infobar.InfoModel;
import application.utils.ExcelRecord;

public class AlbiFileConverter {

	private File fromDirectory, toDirectory;

	public void convertAlbiDeliveryListToExcel(String fromDirectoryPath, String toDirectoryPath) {
		createDirectoriesIfDontExist(fromDirectoryPath, toDirectoryPath);
		for (File f : fromDirectory.listFiles()) {
			InfoModel.getInstance().updateInfo("Pracuju s " + f.getName());
			if (f.getName().contains(".csv")) {
				try {
					writeFile(readAlbiFile(f), toDirectoryPath, f.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				writeFile(readAlbiXLSFile(f), toDirectoryPath, f.getName());
			}
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

	private List<ExcelRecord> readAlbiFile(File file) throws IOException {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		InputStream is = new FileInputStream(file);

		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line.replace("&amp;", "")).append("\n");
			}
		}

		CSVReader reader = new CSVReader(new StringReader(sb.toString()), ';', '\"', 0);
		String[] record;

		while ((record = reader.readNext()) != null) {
			String ean = record[0];
			String amount = record[2];
			String price = record[4].replaceAll(",", ".");

			Double totalPrice = Double.parseDouble(amount) * Double.parseDouble(price);

			records.add(new ExcelRecord(ean, amount, String.format("%.2f", totalPrice)));
		}

		reader.close();

		return records;
	}

	private List<ExcelRecord> readAlbiXLSFile(File file) {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				extractDataAndCreateExcelRecords(records, row);
			}
			wb.close();
		} catch (EncryptedDocumentException | org.apache.poi.openxml4j.exceptions.InvalidFormatException
				| IOException e) {
			System.out.println("Exception thrown in readFile in ExcelUtils");
			e.printStackTrace();
		}
		return records;
	}

	private void extractDataAndCreateExcelRecords(List<ExcelRecord> records, Row row) {
		String ean = row.getCell(6).toString();
		String amount = row.getCell(1).toString().replaceAll("-", "");
		String price = row.getCell(2).toString();
		Double totalPrice = Double.parseDouble(amount) * Double.parseDouble(price);
		records.add(new ExcelRecord(ean, amount.replace(".0", ""), String.format("%.2f", totalPrice)));
	}

	private void writeFile(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		createRowAndAddData(records, sheet);
		resizeColumns(records, sheet);
		createExcelFile(workbook, toDirectoryPath, fileName);
	}

	private void createRowAndAddData(List<ExcelRecord> records, Sheet sheet) {
		int i = 0;
		for (ExcelRecord r : records) {
			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue(r.getEan());
			row.createCell(1).setCellValue(r.getAmount());
			row.createCell(2).setCellValue(r.getPrice());
			i++;
		}
	}

	private void resizeColumns(List<ExcelRecord> records, Sheet sheet) {
		for (int k = 0; k < records.size(); k++) {
			sheet.autoSizeColumn(k);
		}
	}

	private void createExcelFile(Workbook workbook, String toDirectoryPath, String fileName) {
		try {

			FileOutputStream fileOut = null;
			if (fileName.contains(".xls")) {
				fileOut = new FileOutputStream(toDirectoryPath + File.separator + fileName.replace(".xls", ".xlsx"));
			} else {
				fileOut = new FileOutputStream(toDirectoryPath + File.separator + fileName.replace(".csv", ".xlsx"));
			}
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

}
