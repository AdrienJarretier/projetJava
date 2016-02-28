/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;


import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.TextArea;

/**
 *
 * @author COT
 */
public class projection extends TextArea implements Observer{

    
    //Création du TextArea
    //Afficher la chaine de caractère dans le TextArea

    private String Texte;
    
    public projection(){
        super();
        //Rentre la texteArea non éditable
        setEditable(false);
    }
    
   

    @Override
    public void update(Observable o, Object arg) {
        setText("Texte Bidon");
    }

}