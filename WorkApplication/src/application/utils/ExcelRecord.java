package application.utils;

public class ExcelRecord {
	private String ean, amount, price;

	public ExcelRecord(String ean, String amount) {
		this.ean = ean;
		this.amount = amount;
	}

	public ExcelRecord(String ean, String amount, String price) {
		this.ean = ean;
		this.amount = amount;
		this.price = price;
	}

	public String getEan() {
		return this.ean;
	}

	public String getAmount() {
		return this.amount;
	}

	public String getPrice() {
		return this.price;
	}

}
