package application.shared;

public class ExportArticle extends ArticleRow {

	private static final long serialVersionUID = -9167557047115507192L;
	private String exportAmount;

	public ExportArticle(String ean, String name, String soldAmount, String totalAmount, String price, String supplier,
			String dateOfLastSale, String dateOfLastDelivery, String orderOfLastDelivery, String exportAmount) {
		super(ean, name, soldAmount, totalAmount, price, supplier, dateOfLastSale, dateOfLastDelivery,
				orderOfLastDelivery);
		this.exportAmount = exportAmount;
	}

	public String getExportAmount() {
		return this.exportAmount;
	}

	public void setExportAmount(String amount) {
		this.exportAmount = amount;
	}

}