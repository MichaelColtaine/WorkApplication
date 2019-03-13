package application.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.infobar.InfoModel;
import application.utils.ExcelRecord;

public class OmegaFileConverter {
	private File fromDirectory, toDirectory;

	public void convertOmegaDeliveryListToExcel(String fromDirectoryPath, String toDirectoryPath) {
		createDirectoriesIfDontExist(fromDirectoryPath, toDirectoryPath);
		for (File f : fromDirectory.listFiles()) {
			InfoModel.getInstance().updateInfo("Pracuju s " + f.getName());
			writeFile(readOmegaFile(f), toDirectoryPath, f.getName());
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
	// origo funguje
	private List<ExcelRecord> readOmegaFile(File file) {
		List<ExcelRecord> records = new ArrayList<ExcelRecord>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				int index = line.toLowerCase().indexOf(" ks ");
				String ean = line.substring(index + 85, index + 98);
				String amount = line.substring(index + 11, index + 14);
				String price = line.substring(index + 51, index + 58);
				records.add(new ExcelRecord(ean, amount, price.replace(".00", "")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}
	//

//	private List<ExcelRecord> readOmegaFile(File file) {
//		ArrayList<ExcelRecord> records = new ArrayList<>();
//		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//			if (getLineLength(file) > 76) {
//				System.out.println("Long");
//				handleReadingLongLineFile(br, records);
//			} else { // pokud je jeden radek rozdeleny na dva
//				System.out.println("SHORT");
//				handleReadingTwoLinesFile(br, records);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return records;
//	}

//	private void handleReadingLongLineFile(BufferedReader br, ArrayList<ExcelRecord> records) throws IOException {
//		String line;
//		while ((line = br.readLine()) != null) {
//			int index = line.toLowerCase().indexOf(" ks ");
//			String ean = line.substring(index + 85, index + 98);
//			String amount = line.substring(index + 11, index + 14);
//			String price = line.substring(index + 51, index + 58);
//			records.add(new ExcelRecord(ean, amount, price.replace(".00", "")));
//		}
//	}

//	private ArrayList<String> linesArray = new ArrayList<String>();

//	private void handleReadingTwoLinesFile(BufferedReader br, ArrayList<ExcelRecord> records) throws IOException {
//		StringBuilder sb = new StringBuilder();
//		int counter = 0;
//		String line;
//		boolean isFirstLine = true;
//		while ((line = br.readLine()) != null) {
//
////			if ("OMEGA".equals(line.substring(0, 5)) || isFirstLine) {
////				linesArray.add(sb.toString());
////				sb.delete(0, sb.length());
////				isFirstLine = false;
////				sb.append(line);
////			} else {
////				sb.append(line);
////
////			}
//			
//			sb.append(line);
//
//		}
//		
//		System.out.println(sb.toString());
//
////		linesArray.forEach((a) -> System.out.println(a));
//	}

	private int getLineLength(File file) {
		int lineLength = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			lineLength = line.length();
			System.out.println(lineLength);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return lineLength;
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
