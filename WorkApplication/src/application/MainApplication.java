package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

	private String START_LOCATION = "/application/ui/Home.fxml";
	private double xOffset = 0, yOffset = 0;

	@Override
	public void start(Stage primaryStage) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource(START_LOCATION));
			root.setOnMousePressed(event -> setOffsets(event));
			root.setOnMouseDragged(event -> calculatePosition(primaryStage, event));
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/application/main.css");
			primaryStage.setTitle("Skladový pomocník");
			primaryStage.getIcons().add(new Image("/images/icons8_Trolley_52px.png"));
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculatePosition(Stage primaryStage, MouseEvent event) {
		primaryStage.setX(event.getScreenX() - xOffset);
		primaryStage.setY(event.getScreenY() - yOffset);
	}

	private void setOffsets(MouseEvent event) {
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
