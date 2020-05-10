package application.utils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/*
 * This class takes PDF file and extract all text from it into a string.
 */

public class PdfWorker {

	public PdfWorker() {

	}

	public String getText(String input) {
		try {
			PDDocument document = loadFile(input);
			String text = retrieveText(document);
			document.close();
			return text;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private PDDocument loadFile(String fileName) throws IOException {
		File file = new File(fileName);
		return PDDocument.load(file);
	}

	private String retrieveText(PDDocument document) throws IOException {
		PDFTextStripper PDFStripper = new PDFTextStripper();
		return PDFStripper.getText(document);
	}

}
