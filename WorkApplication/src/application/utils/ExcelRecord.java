package application.utils;

public class ExcelRecord {
	private String ean, amount;

	public ExcelRecord(String ean, String amount) {
		this.ean = ean;
		this.amount = amount;
	}

	public String getEan() {
		return this.ean;
	}

	public String getAmount() {
		return this.amount;
	}

}
