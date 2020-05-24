package vadique.khpi.year2.stats.third;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FisherMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
        	BorderPane root = FXMLLoader.load(getClass().getResource("FisherWindow.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Fisher Hypothesis Testing");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
