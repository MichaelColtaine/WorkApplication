package application.scanner;

class ServerArticle {

	private String ean;
	private String amount;

	public ServerArticle(String ean, String amount) {
		this.ean = ean;
		this.amount = amount;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEan() {
		return this.ean;
	}

	public String getAmount() {
		return this.amount;
	}

}
