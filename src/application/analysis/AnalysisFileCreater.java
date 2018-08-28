package application.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AnalysisFileCreater {
	private String path = AnalysisModel.getInstance().getSettings().getPath() + File.separator;
	private String data;
	private String[] returns, orders;

	public AnalysisFileCreater(String data) {

		this.data = data;
	}

	public void createExcelReturnsFile(String[] data) {
		returns = data;
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();
		createFirstRow(sheet1, "Vratka");
		int i = 1;
		for (String s : returns) {
			String[] temp = s.split(";");
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(temp[0]);
			row.createCell(1).setCellValue(temp[1]);
			row.createCell(2).setCellValue(temp[2]);
			row.createCell(3).setCellValue(temp[3]);
			row.createCell(4).setCellValue(temp[4]);
			row.createCell(5).setCellValue(temp[5]);
			row.createCell(6).setCellValue(temp[6]);
			row.createCell(7).setCellValue(temp[7]);
			row.createCell(8).setCellValue(temp[8]);
			row.createCell(9).setCellValue(temp[9]);
			i++;
		}

		for (int k = 0; k <= returns.length; k++) {
			sheet1.autoSizeColumn(k);
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(path + File.separator + "VR_" + returnDate() + ".xlsx");
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createExcelOrderFile(String[] data) {
		orders = data;
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();
		createFirstRow(sheet1, "Objednávka");
		int i = 1;
		for (String s : orders) {
			String[] temp = s.split(";");
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(temp[0]);
			row.createCell(1).setCellValue(temp[1]);
			row.createCell(2).setCellValue(temp[2]);
			row.createCell(3).setCellValue(temp[3]);
			row.createCell(4).setCellValue(temp[4]);
			row.createCell(5).setCellValue(temp[5]);
			row.createCell(6).setCellValue(temp[6]);
			row.createCell(7).setCellValue(temp[7]);
			row.createCell(8).setCellValue(temp[8]);
			row.createCell(9).setCellValue(temp[9]);
			i++;
		}

		for (int k = 0; k <= orders.length; k++) {
			sheet1.autoSizeColumn(k);
		}

		try {

			FileOutputStream fileOut = new FileOutputStream(path + File.separator + "OBJ_" + returnDate() + ".xlsx");
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String returnDate() {
		StringBuilder sb = new StringBuilder();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(mili);
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DAY_OF_MONTH);
		// calendar.ti
		DateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH-MM-SS");
		Date date = new Date();
		System.out.println(sdf.format(date));

		// return sb.toString();
		return sdf.format(date).toString();
	}

	private void createFirstRow(Sheet sheet1, String text) {
		Row row = sheet1.createRow(0);
		row.createCell(0).setCellValue("EAN");
		row.createCell(1).setCellValue("Název");
		row.createCell(2).setCellValue("Prodeje");
		row.createCell(3).setCellValue("StavSkladu");
		row.createCell(4).setCellValue("DPC");
		row.createCell(5).setCellValue("Dodavatel");
		row.createCell(6).setCellValue("Datum posledního prodeje");
		row.createCell(7).setCellValue("Datum posledního pøíjmu");
		row.createCell(8).setCellValue("Øada posledního pøíjmu");
		row.createCell(9).setCellValue(text);
	}

}
