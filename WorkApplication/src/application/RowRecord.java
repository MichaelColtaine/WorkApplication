package application;

import javafx.beans.property.SimpleStringProperty;

public class RowRecord {

	private SimpleStringProperty deliveryNote, fileName, rabat;

	public RowRecord(String deliveryNote, String fileName, String rabat) {
		this.deliveryNote = new SimpleStringProperty(deliveryNote);
		this.fileName = new SimpleStringProperty(fileName);
		this.rabat = new SimpleStringProperty(rabat);
	}

	public String getDeliveryNote() {
		return this.deliveryNote.get();
	}

	public String getFileName() {
		return this.fileName.get();
	}

	public String getRabat() {
		return this.rabat.get();
	}

}
