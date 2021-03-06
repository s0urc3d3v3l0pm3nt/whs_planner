package WHS_planner.CoreUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class Login extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception{


        String sceneFile = "CoreUI" + File.separator + "loginPage.fxml";
        Parent root = null;
        URL    url  = null;

        try
        {
            FXMLLoader loader = new FXMLLoader();
            url = getClass().getResource("loginPage.fxml");
            root = FXMLLoader.load(url);
            System.out.println( "  fxmlResource = " + sceneFile );
        }
        catch ( Exception ex )
        {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "  * url: " + url );
            System.out.println( "  * " + ex );
            System.out.println( "    ----------------------------------------\n" );
            throw ex;
        }



        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
