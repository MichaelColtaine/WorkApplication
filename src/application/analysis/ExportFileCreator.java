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

import application.shared.ExportArticle;

public class ExportFileCreator {
	private String path = AnalysisModel.getInstance().getSettings().getPath() + File.separator;

	public ExportFileCreator() {

	}

	public void createFiles() {
		if (!AnalysisModel.getInstance().getReturns().isEmpty()) {
			createExcelReturnsFile();
		}

		if (!AnalysisModel.getInstance().getOrders().isEmpty()) {
			createExcelOrdersFile();
		}

	}

	public void createExcelReturnsFile() {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();
		createFirstRow(sheet1, "Vratka");
		int i = 1;
		for (ExportArticle e : AnalysisModel.getInstance().getReturns()) {
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(e.getEan());
			row.createCell(1).setCellValue(e.getName());
			row.createCell(2).setCellValue(e.getSoldAmount());
			row.createCell(3).setCellValue(e.getTotalAmount());
			row.createCell(4).setCellValue(e.getPrice());
			row.createCell(5).setCellValue(e.getSupplier());
			row.createCell(6).setCellValue(e.getDateOfLastSale());
			row.createCell(7).setCellValue(e.getDateOfLastDelivery());
			row.createCell(8).setCellValue(e.getOrderOfLastDelivery());
			row.createCell(9).setCellValue(e.getExportAmount());
			i++;
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

	public void createExcelOrdersFile() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();
		createFirstRow(sheet1, "Objednávka");
		int i = 1;
		for (ExportArticle e : AnalysisModel.getInstance().getOrders()) {
			Row row = sheet1.createRow(i);
			row.createCell(0).setCellValue(e.getEan());
			row.createCell(1).setCellValue(e.getName());
			row.createCell(2).setCellValue(e.getSoldAmount());
			row.createCell(3).setCellValue(e.getTotalAmount());
			row.createCell(4).setCellValue(e.getPrice());
			row.createCell(5).setCellValue(e.getSupplier());
			row.createCell(6).setCellValue(e.getDateOfLastSale());
			row.createCell(7).setCellValue(e.getDateOfLastDelivery());
			row.createCell(8).setCellValue(e.getOrderOfLastDelivery());
			row.createCell(9).setCellValue(e.getExportAmount());
			i++;
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
		DateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH-MM-SS");
		Date date = new Date();
		System.out.println(sdf.format(date));
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
