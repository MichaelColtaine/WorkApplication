package application.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.RowRecord;
import application.albatros.AlbatrosModel;
import application.beta.BetaModel;
import application.euromedia.EuroModel;
import application.kosmas.KosmasModel;

public class ExcelUtils {

	private File fromDirectory, toDirectory;

	public void euromediaExcel(String fromDirectoryPath, String toDirectoryPath) {
		createDirectoriesIfDontExist(fromDirectoryPath, toDirectoryPath);
		for (File f : fromDirectory.listFiles()) {
			StringBuilder sb = new StringBuilder().append(f.getName().substring(0, f.getName().length() - 3))
					.append("xlsx");
			EuroModel.getInstance().getRecords()
					.add(new RowRecord(sb.toString().substring(4, sb.toString().length() - 5), "", ""));
			writeFile(readFile(f), toDirectoryPath, sb.toString());
		}
	}

	public void albatrosExcel() {
		File directory = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator);
		for (File f : directory.listFiles()) {
			StringBuilder sb = new StringBuilder().append(f.getName().substring(6));
			AlbatrosModel.getInstance().getListOfNames()
					.add(new RowRecord(sb.toString().substring(0, sb.toString().length() - 5), "", ""));
			writeFile(readFileAlbatros(f), AlbatrosModel.getInstance().getSettings().getPath(), sb.toString());
		}
	}

	public void kosmasExcel() {
		File directory = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator);
		for (File f : directory.listFiles()) {
			List<ExcelRecord> records = new ArrayList<ExcelRecord>();
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = br.readLine()) != null) {
					int index = line.indexOf("ks");
					String amount = line.substring(index + 9, index + 16);
					amount = amount.substring(0, amount.indexOf("."));
					String ean = line.substring(index + 81, index + 94);
					records.add(new ExcelRecord(ean, amount));
					writeFile(records, KosmasModel.getInstance().getSettings().getPath(),
							f.getName().replaceAll(".txt", ".xlsx"));
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			KosmasModel.getInstance().getFileNames().add(new RowRecord(f.getName().replace(".txt", ""), "", ""));
		}
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
				records.add(new ExcelRecord(new BigDecimal(row.getCell(6).toString()).toPlainString(), amountAsString));
			}
			wb.close();

		} catch (EncryptedDocumentException | org.apache.poi.openxml4j.exceptions.InvalidFormatException
				| IOException e) {
			System.out.println("readFile in ExcelUtils");
			e.printStackTrace();
		}
		return records;
	}

	public void betaExcel() {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		File directory = new File(BetaModel.getInstance().getFromPath());
		for (File f : directory.listFiles()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line;
				String ean = "", amount = "";
				while ((line = br.readLine()) != null) {
					if (line.contains("Ks")) {
						amount = line.substring(56, 59);
					}
					if (line.contains("0.09")) {
						ean = line.substring(line.indexOf("0.09") + 3, line.indexOf("0.09") + 16);
					}
					if (!amount.isEmpty() && !ean.isEmpty()) {

						records.add(new ExcelRecord(ean, amount.replace(".0", "")));
						ean = "";
						amount = "";
					}
				}
				writeFile(records, BetaModel.getInstance().getToPath(),
						f.getName().substring(0, f.getName().length() - 3) + "xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			records.clear();
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

	private List<ExcelRecord> readFile(File file) {
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
				records.add(new ExcelRecord(new BigDecimal(row.getCell(7).toString()).toPlainString(), amountAsString));
			}
			wb.close();

		} catch (EncryptedDocumentException | org.apache.poi.openxml4j.exceptions.InvalidFormatException
				| IOException e) {
			System.out.println("readFile in ExcelUtils");
			e.printStackTrace();
		}
		return records;
	}

	private void writeFile(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();

		int i = 0;
		for (ExcelRecord r : records) {
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(r.getEan());
			row.createCell(1).setCellValue(r.getAmount());
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
