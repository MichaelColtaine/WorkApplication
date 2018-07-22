package application.scanner;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Article {

	private SimpleStringProperty ean;
	private SimpleIntegerProperty amount;
	private int counter = 1;

	public Article(String ean) {
		this.ean = new SimpleStringProperty(ean);
		this.amount = new SimpleIntegerProperty(1);
	}

	public String getAmount() {
		return this.amount.get() + "";
	}

	public String getEan() {
		return this.ean.get();
	}

	public void increaseAmount() {
		this.counter++;
		this.amount.set(counter);
	}

	public void editAmountValue(String newValue) {
		System.out.println("Amount before : " + amount.get() + " counter " + counter);
		this.amount.set(Integer.parseInt(newValue));
		this.counter = Integer.parseInt(newValue);
		System.out.println("Amount after : " + amount.get() + " counter " + counter);
	}

}
