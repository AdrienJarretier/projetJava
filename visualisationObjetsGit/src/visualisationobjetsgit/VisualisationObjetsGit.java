/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualisationobjetsgit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author freeze
 */
public class VisualisationObjetsGit extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        StackPane root = new StackPane();
        
        Scene scene = new Scene(root, 1280, 720);
        
        primaryStage.setTitle("Visualisation objets git");
        primaryStage.setScene(scene);
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setVgap(5);
        grid.setHgap(5);

        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        GridPane.setConstraints(searchField, 0, 0);
        grid.getChildren().add(searchField);
        
        Button searchButton = new Button("Search");
        GridPane.setConstraints(searchButton, 1, 0);
        grid.getChildren().add(searchButton);
        
        root.getChildren().add(grid);
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
