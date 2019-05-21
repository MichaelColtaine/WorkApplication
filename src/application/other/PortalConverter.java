package application.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.SplitMapUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.infobar.InfoModel;
import application.utils.ExcelRecord;

public class PortalConverter {

	private File fromDirectory, toDirectory;

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


	private List<ExcelRecord> readPortalFile(File file) {
		ArrayList<ExcelRecord> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			if (getLineLength(file) > 69) {
				handleReadingLongLineFile(br, records);
			} else { // pokud je jeden radek rozdeleny na tri
				handleReadingThreeLinesFile(br, records);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return records;
	}
	
	private int getLineLength(File file) {
		int lineLength = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			lineLength = line.length();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return lineLength;
	}

	private void handleReadingLongLineFile(BufferedReader br, ArrayList<ExcelRecord> records) throws IOException {
		String line = "";
		while ((line = br.readLine()) != null) {
			String ean = line.substring(110, 142).replaceAll(" ", "");
			String amount = line.substring(54, 60).replaceAll(" ", "").replaceAll("\\.0", "");
			String price = line.substring(93, 103).replace(".00", "").replaceAll(" ", "");
			records.add(new ExcelRecord(ean, amount, price));
		}

	}

	private void handleReadingThreeLinesFile(BufferedReader br, ArrayList<ExcelRecord> records) throws IOException {
		StringBuilder sb = new StringBuilder();
		int counter = 1;
		String line;
		while ((line = br.readLine()) != null) {
			if (counter == 3) {
				sb.append(line).append("\n");
				String ean = sb.toString().substring(110, 125).trim();
				String amount = sb.toString().substring(53, 60).replace(".0", "").trim();
				String price = sb.toString().substring(75, 90).trim().replace(".00", "");
				records.add(new ExcelRecord(ean, amount, price));
				sb.delete(0, sb.length());
				counter = 1;
				continue;
			} else {
				sb.append(line);
				counter++;
			}
		}
	}

	public void convertPortalDeliveryListToExcel(String fromDirectoryPath, String toDirectoryPath) {
		createDirectoriesIfDontExist(fromDirectoryPath, toDirectoryPath);
		for (File f : fromDirectory.listFiles()) {
			InfoModel.getInstance().updateInfo("Pracuju s " + f.getName());
			writeFile(readPortalFile(f), toDirectoryPath, f.getName());
			f.delete();
		}
	}

	private void writeFile(List<ExcelRecord> records, String toDirectoryPath, String fileName) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		createRowAndAddData(records, sheet);
		createExcelFile(workbook, toDirectoryPath, fileName);
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

	private void createExcelFile(Workbook workbook, String toDirectoryPath, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(
					toDirectoryPath + File.separator + fileName.replace(".txt", ".xlsx"));
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
