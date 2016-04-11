package views;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javax.imageio.ImageIO;
import model.Git;
import model.GitObject;
import model.GitObjectProperty;

public class GitObjectFileContentViewTextFlow extends TextFlow implements Observer{
    
    private Git model;
    // on relie cette vue a la liste des objets pour changer la selection
    private GitObjectsFilesTreeView treeView;
    
    public GitObjectFileContentViewTextFlow( Git model, GitObjectsFilesTreeView treeView ){
        
        super( new Text("Ouvrez un depôt Git par le menu") );
        
        this.model = model;
        this.treeView = treeView;
        
        setPadding( new Insets( 8 ) );
    
        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        
        GitObject so = model.getSelectObject();
        
        getChildren().clear();
        
        if( so == null ) {
            getChildren().add( new Text("Selectionnez un objet dans la liste sur la gauche.") );
        }
        else {
            try {
                
                getChildren().add( new Text( "Name : " + so.getName() ) );
                getChildren().add( new Text( System.getProperty("line.separator") ) );
                getChildren().add( new Text( System.getProperty("line.separator") ) );
                
                for (GitObjectProperty property : so.getProperties()) {
                    String pName = new String();
                    if( property.name.length() > 0 ) {
                        pName = property.name.substring(0, 1).toUpperCase() + property.name.substring(1);
                    }
                    switch(property.type) {
                        case STRING:
                            getChildren().add( new Text( pName + " : " ) );
                            getChildren().add( new Text( (String)property.value ) );
                            break;
                            
                        case STRING_BLOC:
                            getChildren().add( new Text( pName + " : " ) );
                            getChildren().add( new Text( System.getProperty("line.separator") ) );
                            getChildren().add( new Text( (String)property.value ) );
                            break;
                            
                        case OBJECT_REF:
                            getChildren().add( new Text( pName + " : " ) );
                            if( property.value == null ) {
                                getChildren().add( new Text( "null (dans pack en delta)" ) );
                            }
                            else {
                                Hyperlink link = new Hyperlink( ((GitObject)property.value).getName() );
                                link.setOnAction((ActionEvent e) -> {
                                    
                                    treeView.selectByGitObject( (GitObject)property.value );
                                    
                                });
                                getChildren().add( link );
                            }
                            break;
                            
                        case IMAGE:
                            
                            Byte[] value = (Byte[])property.value;
                            
                            byte[] pixels = new byte[ value.length ];
                            
                            for (int i = 0; i < value.length; i++) {
                                
                                pixels[i] = value[i];
                            }
                            
                            ByteArrayInputStream is = new ByteArrayInputStream( pixels );
                            Image image = new Image(is);
                            
                            getChildren().add( new ImageView(image) );
                            break;
                            
                        case UNKNOWN:
                            getChildren().add( new Text("type de fichier inconnu") );
                            getChildren().add( new Text( System.getProperty("line.separator") ) );
                            getChildren().add( new Text("debut du fichier en hexadecimal :") );
                            getChildren().add( new Text( System.getProperty("line.separator") ) );
                            getChildren().add( new Text( System.getProperty("line.separator") ) );
                            String sValue = (String)property.value;
                            getChildren().add( new Text( sValue.substring(0, sValue.length()>2047 ? 2047 : sValue.length()-1) ) );
                            break;
                    }
                    getChildren().add( new Text( System.getProperty("line.separator") ) );
                }
            } catch (IOException ex) {
                getChildren().add( new Text(ex.getMessage()) );
            }
        }
    }

}