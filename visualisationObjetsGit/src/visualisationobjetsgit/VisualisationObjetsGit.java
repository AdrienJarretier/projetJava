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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author freeze
 */
public class VisualisationObjetsGit extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visualisation objets git");
        VBox root = new VBox();
        Scene scene = new Scene(root, 1280, 720);

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);

        MenuItem menuFileOpen = new MenuItem("open");




        root.getChildren().add(menuBar);
        

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
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
