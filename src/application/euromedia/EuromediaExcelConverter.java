package application.euromedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.RowRecord;
import application.infobar.InfoModel;
import application.utils.ExcelRecord;

public class EuromediaExcelConverter {
	private File fromDirectory, toDirectory;

	public void euromediaExcel(String fromDirectoryPath, String toDirectoryPath) {
		createDirectoriesIfDontExist(fromDirectoryPath, toDirectoryPath);
		for (File f : fromDirectory.listFiles()) {
			StringBuilder sb = new StringBuilder().append(f.getName().substring(0, f.getName().length() - 3))
					.append("xlsx");
			InfoModel.getInstance().updateInfo(f.getName());
			EuroModel.getInstance().getRecords().add(new RowRecord(sb.toString().replaceAll(".xlsx", ""), "", ""));
			writeFileFourInputs(readFileEuromedia(f), toDirectoryPath, sb.toString().replace("XLS_", ""));
		}
	}

	private List<ExcelRecord> readFileEuromedia(File file) {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				Double convertAmountToDouble = Double
						.parseDouble(new BigDecimal(row.getCell(11).toString()).toPlainString());
				Integer convertAmountToInt = convertAmountToDouble.intValue();
				String amountAsString = String.valueOf(convertAmountToInt);
				String price = row.getCell(8).toString();
				price = price.substring(0, price.length() - 2);
				double pricePerUnit = row.getCell(9).getNumericCellValue();

				double totalPrice = pricePerUnit * convertAmountToInt;
				records.add(new ExcelRecord(new BigDecimal(row.getCell(7).toString()).toPlainString(), amountAsString,
						price, totalPrice));
			}
			wb.close();

		} catch (EncryptedDocumentException | org.apache.poi.openxml4j.exceptions.InvalidFormatException
				| IOException e) {
			System.out.println("readFile in ExcelUtils");
			e.printStackTrace();
		}
		return records;
	}

	private void writeFileFourInputs(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();

		int i = 0;
		for (ExcelRecord r : records) {
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(r.getEan());
			row.createCell(1).setCellValue(r.getAmount());
			row.createCell(3).setCellValue(r.getPrice());
			row.createCell(2).setCellValue(r.getTotalPrice());
			i++;
		}

		for (int k = 0; k < records.size(); k++) {
			sheet1.autoSizeColumn(k);
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(toDirectoryPath + File.separator + fileName);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
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

}
