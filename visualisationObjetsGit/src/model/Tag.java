package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Tag extends GitObject{
    
    public Tag(File _file) throws IOException {
        
        super(_file);
        

        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("nom : " + this.getName());
//        System.out.println( FileReading.stringValue( FileReading.ReadFile(_file)));
//        System.out.println("-------------------------------------------------------");
        
        
    }
    
}
