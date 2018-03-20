package application.infobar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InfoString {

	private StringProperty info = new SimpleStringProperty(this, "Info", "");

	public final String getInfo() {
		return info.get();
	}

	public final void setInfo(String text) {
		this.info.set(text);
	}

	public final StringProperty getInfoProperty() {
		return info;
	}

}
