package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
<<<<<<< HEAD
import controllers.ModifyBookViewController;

=======
>>>>>>> refs/heads/Acounts_Manager

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManagerPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.setMaximized(false);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}