package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Tree extends GitObject{
    
    private ArrayList<GitObject> childs;
    
    public Tree(File _file) throws IOException {
        
        super(_file);
        
        
        System.out.println("-------------------------------------------------------");
        System.out.println("nom : " + this.getName());
        System.out.println(stringValue( FileReading.ReadFile(_file) ));
        System.out.println("-------------------------------------------------------");

    }
    // lecture de l'arbre de façon lisible pour un humain
     public static String stringValue( Byte[] inflated ) {
        
        StringBuilder content = new StringBuilder();
        
        int i = 0;
        char c;
        // affichage de l'entete
        do {
            c = (char)inflated[i].byteValue();
            content.append(c);
            i++;
        }while (c != '\0');
        content.append('\n');
        
        // pour chaque entree on fait la meme chose
        while(i < inflated.length) {
            // affichage du nom
            do {
                c = (char)inflated[i].byteValue();
                content.append(c);
                i++;
            }while (c != '\0');
            content.append(" ");
            
            // recuperation de la cle des fichiers que l'arbre contient
            for(int j = 0; j<20; j++){
                content.append(String.format("%02x", inflated[i]));
                i++;
            } 
            content.append('\n');
        }
        
        return content.toString();
        
    }
    
}
