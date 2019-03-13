/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejava;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author safwat
 */
public class GameJava extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
         FXMLLoader x = new FXMLLoader();
        // x.setLocation(getClass().getResource("player.fxml"));
         x.setLocation(getClass().getResource("login.fxml"));
         Parent root = x.load();
        //playerController c = x.getController();
       // c.prepareConnection();
       
//        Parent root = new FXMLDocumentBase();
        Scene scene = new Scene(root);
         stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
