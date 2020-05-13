package application.albatros;

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

public class AlbatrosExcelConverter {

	private String deliveryNoteName;

	public void albatrosExcel() {
		File tempDirectory = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator);
		for (File f : tempDirectory.listFiles()) {
			writeFileFourInputs(readFileAlbatros(f), AlbatrosModel.getInstance().getSettings().getPath(),
					deliveryNoteName + ".xlsx");
			InfoModel.getInstance().updateInfo("Převádím " + deliveryNoteName);
			AlbatrosModel.getInstance().getListOfNames().add(new RowRecord(deliveryNoteName, "", ""));
		}
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private List<ExcelRecord> readFileAlbatros(File file) {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				Double convertAmountToDouble = Double
						.parseDouble(new BigDecimal(row.getCell(10).toString()).toPlainString());
				Integer convertAmountToInt = convertAmountToDouble.intValue();
				String amountAsString = String.valueOf(convertAmountToInt);
				String price = row.getCell(7).toString();
				price = price.substring(0, price.length() - 2);
				this.deliveryNoteName = row.getCell(1).toString();
				
				
				
				double pricePerunit = row.getCell(8).getNumericCellValue();
				double totalPrice = Double.parseDouble(amountAsString) * pricePerunit;

				records.add(new ExcelRecord(new BigDecimal(row.getCell(6).toString()).toPlainString(), amountAsString,
						price, totalPrice));
			}
			wb.close();

		} catch (EncryptedDocumentException 
				| IOException e) {
			System.out.println("readFile in AlbatrosExcelConverter");
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

}
