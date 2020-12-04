package stratego;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	/***
	 * <p>Pop up window showing the result.
	 * 
	 * @param message is the message to display
	 * 
	 * @author Ahmed Aldawood
	 */
	public static void display(String message) {
		Stage stage = new Stage();
		
		stage.initModality(Modality.APPLICATION_MODAL);
		//label
		Label label = new Label(message);
		//button setup
		Button button = new Button();
		button.setText("OK");
		//button position
		button.setAlignment(Pos.BOTTOM_RIGHT);
		//close window when button is clicked on
		button.setOnAction(e -> stage.close());
		//VBox set up
		VBox layout = new VBox(20);
		//add to layout
		layout.getChildren().addAll(label, button);
		//layout position
		layout.setAlignment(Pos.CENTER);
		//add layout to scene and make scene 200x150
		Scene scene = new Scene(layout,200,150);
		//set scene on stage
		stage.setScene(scene);
		//set stage title
		stage.setTitle("Results");
		//show stage and wait for user to click button or exit
		stage.showAndWait();
	}
}
