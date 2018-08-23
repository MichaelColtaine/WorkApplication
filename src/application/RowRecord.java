package application;

import javafx.beans.property.SimpleStringProperty;

public class RowRecord {

	private SimpleStringProperty deliveryNoteName, fileName, rabat;

	public RowRecord(String deliveryNote, String fileName, String rabat) {
		this.deliveryNoteName = new SimpleStringProperty(deliveryNote);
		this.fileName = new SimpleStringProperty(fileName);
		this.rabat = new SimpleStringProperty(rabat);
	}

	public String getDeliveryNote() {
		return this.deliveryNoteName.get();
	}

	public String getFileName() {
		return this.fileName.get();
	}

	public String getRabat() {
		return this.rabat.get();
	}

}
