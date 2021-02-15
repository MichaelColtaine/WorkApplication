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

import com.mtr.application.shared.ExportArticle;

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
		int rowNumber = 1;
		for (ExportArticle e : AnalysisModel.getInstance().getReturns()) {
			createRow(sheet1, rowNumber, e);
			rowNumber++;
		}
		createFile(workbook, "VR_");
	}
	
	public void createExcelOrdersFile() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet();
		createFirstRow(sheet1, "Objednávka");
		int rowNumber = 1;
		for (ExportArticle e : AnalysisModel.getInstance().getOrders()) {
			createRow(sheet1, rowNumber, e);
			rowNumber++;
		}
		createFile(workbook,  "OBJ_");
	}
	
	private void createFirstRow(Sheet sheet1, String text) {
		Row row = sheet1.createRow(0);
		row.createCell(0).setCellValue("Poøadí");
		row.createCell(1).setCellValue("Kód");
		row.createCell(2).setCellValue("Ean");
		row.createCell(3).setCellValue("Název");
		row.createCell(4).setCellValue("Prodej");
		row.createCell(5).setCellValue("Obrat");
		row.createCell(6).setCellValue("Stav skladu");
		row.createCell(7).setCellValue("Lokace");
		row.createCell(8).setCellValue("DPC");
		row.createCell(9).setCellValue("Dodavatel");
		row.createCell(10).setCellValue("Autor");
		row.createCell(11).setCellValue("Datum posledního prodeje");
		row.createCell(12).setCellValue("Datumm posledního pøíjmu");
		row.createCell(13).setCellValue("Datum vydání");
		row.createCell(14).setCellValue("Øada posledního pøíjmu");
		row.createCell(15).setCellValue("Poøadí eshop");
		row.createCell(16).setCellValue(text);
	}

	
	private void createRow(Sheet sheet1, int rowNumber, ExportArticle e ){
		Row row = sheet1.createRow(rowNumber);
		row.createCell(0).setCellValue(e.getRank());
		row.createCell(1).setCellValue(e.getFirstCode());
		row.createCell(2).setCellValue(e.getEan());
		row.createCell(3).setCellValue(e.getName());
		row.createCell(4).setCellValue(e.getSales());
		row.createCell(5).setCellValue(e.getRevenue());
		row.createCell(6).setCellValue(e.getStoredAmount());
		row.createCell(7).setCellValue(e.getLocations());
		row.createCell(8).setCellValue(e.getPrice());
		row.createCell(9).setCellValue(e.getSupplier());
		row.createCell(10).setCellValue(e.getAuthor());
		row.createCell(11).setCellValue(e.getDateOfLastSale());
		row.createCell(12).setCellValue(e.getDateOfLastDelivery());
		row.createCell(13).setCellValue(e.getRealeaseDate());
		row.createCell(14).setCellValue(e.getDeliveredAs());
		row.createCell(15).setCellValue(e.getEshopRank());
		row.createCell(16).setCellValue(e.getExportAmount());
	}
	
	private void createFile(Workbook workbook, String prefix){
		try {
			FileOutputStream fileOut = new FileOutputStream(path + File.separator + prefix + returnDate() + ".xlsx");
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


}
