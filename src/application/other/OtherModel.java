package application.other;

import application.utils.ExcelUtils;

public class OtherModel {

	private static OtherModel INSTANCE;
	private OtherSettings settings = new OtherSettings();

	public OtherModel() {

	}

	public static OtherModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OtherModel();
		}
		return INSTANCE;
	}

	public void handleGradaButton() {
		GradaFileConverter grada = new GradaFileConverter();
		grada.convertGradaDeliveryListToExcel(settings.getFromPath(), settings.getToPath());
	}

	public OtherSettings getSettings() {
		return this.settings;
	}

	public void savePaths(String from, String to) {
		settings.savePaths(from, to);
	}

	public String getFromPath() {
		return settings.getFromPath();
	}

	public String getToPath() {
		return settings.getToPath();
	}

	public void handleMarcoButton() {
		MarcoPoloFileConverter marco = new MarcoPoloFileConverter();
		marco.convertMarcoDeliviryNoteToExcel(settings.getFromPath(), settings.getToPath());
	}

	public void handlePemicButton() {
		PemicFileConverter pemic = new PemicFileConverter();
		pemic.convertPemicDeliveryNoteToExcel();
	}

	public void handleOmegaButton() {
		OmegaFileConverter omega = new OmegaFileConverter();
		omega.convertOmegaDeliveryListToExcel(settings.getFromPath(), settings.getToPath());
	}

	public void handlePrescoButton() {
		ExcelUtils excel = new ExcelUtils();
		excel.prescoExcel(settings.getFromPath(), settings.getToPath());
	}
	
	public void handlePortalButton() {
		PortalConverter portal = new PortalConverter();
		portal.convertPortalDeliveryListToExcel(settings.getFromPath(), settings.getToPath());
	}

}
