package application.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.utils.ExcelRecord;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;

public class PemicFileConverter {

	private Map<String, List<ExcelRecord>> files = new HashMap<>();

	public PemicFileConverter() {

	}

	public void convertPemicDeliveryNoteToExcel() {
		File fromDirectory = new File(OtherModel.getInstance().getFromPath());
		for (File f : fromDirectory.listFiles()) {
			if (f.getName().toUpperCase().equals("DL.DBF")) {
				readFile(f);
				f.delete();
			}
		}

		for (String fileName : files.keySet()) {
			List<ExcelRecord> records = files.get(fileName);
			writeFileThreeInputs(records, OtherModel.getInstance().getToPath(), fileName + ".xlsx");
		}

	}

	private void readFile(File f) {
		Charset stringCharset = Charset.forName("Windows-1250");
		DbfRecord rec;
		try (DbfReader reader = new DbfReader(f)) {
			while ((rec = reader.read()) != null) {
				rec.setStringCharset(stringCharset);
				String numberOfDeliveryNote = rec.getString("CISDOKL");
				if (!files.containsKey(numberOfDeliveryNote)) {
					files.put(numberOfDeliveryNote, new ArrayList<>());
				}
				numberOfDeliveryNote = rec.getString("CISDOKL");
				String ean = rec.getString("EAN");
				String amount = rec.getString("MNOZSTVI").replaceAll(".00000", "");
				BigDecimal actionPrice = rec.getBigDecimal("AKCNICENA");
				BigDecimal price = rec.getBigDecimal("MOC");
				String currentPrice = "";
				if (actionPrice.signum() == 0) {
					currentPrice = price.toPlainString();
				} else {
					currentPrice = actionPrice.toString();
				}
				files.get(numberOfDeliveryNote)
						.add(new ExcelRecord(ean, amount, currentPrice.replaceAll(".00000", "")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFileThreeInputs(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		createAndFillRows(records, sheet);
		resizeColumns(records, sheet);
		createExcelFile(workbook, toDirectoryPath, fileName);
	}

	private void createAndFillRows(List<ExcelRecord> records, Sheet sheet) {
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

	private void createExcelFile(Workbook workbook, String toDirectoryPath, String fileName) {
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
