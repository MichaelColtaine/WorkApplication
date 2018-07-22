package application.scanner;

public class ExcelRecord {
	private String ean, amount, price;
	private double totalPrice;

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

	public String getPrice() {
		return this.price;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

}
