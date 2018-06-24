package application.beta;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class BetaModel {

	private static BetaModel INSTANCE;
	private BetaFileMover mover = new BetaFileMover();
	private BetaSettings settings = new BetaSettings();

	private BetaModel() {

	}

	public static BetaModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BetaModel();
		}
		return INSTANCE;
	}

	public void moveAndRename() {
		try {
			mover.setFromDirectory(settings.getFromPath() + "\\");
			mover.setToDirectory(settings.getToPath() + "\\");
			mover.move();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
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

	public boolean isFromDirectoryEmpty() {
		return (mover.getFromDirectory() == null) || (mover.getFromDirectory().length() == 0);
	}

}
