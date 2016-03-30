package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Tree extends GitObject{
    
    private ArrayList<GitObject> childs;
    
    public Tree(File _file) throws IOException {
        
        super(_file);
        
        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("nom : " + this.getName());
//        System.out.println(stringValue( FileReading.ReadFile(_file) ));
//        System.out.println("-------------------------------------------------------");

    }
    
     public static String stringValue( Byte[] inflated ) {
        
        StringBuilder content = new StringBuilder();
        
        int i = 0;
        char c;
        do {
            c = (char)inflated[i].byteValue();
            content.append(c);
            i++;
        }while (c != '\0');
        content.append('\n');
        
        while(i < inflated.length) {
            do {
                c = (char)inflated[i].byteValue();
                content.append(c);
                i++;
            }while (c != '\0');
            content.append(" ");
            
            for(int j = 0; j<20; j++){
                content.append(String.format("%02x", inflated[i]));
                i++;
            } 
            content.append('\n');
        }
        
        return content.toString();
        
    }
    
}
