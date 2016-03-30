package model;

import java.io.File;
import java.io.IOException;


public class Blob extends GitObject{
    

    public Blob(File _file) throws IOException {
        

        super(_file);
        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("nom : " + this.getName());
//        System.out.println(FileReading.stringValue( FileReading.ReadFile(_file) ));
//        System.out.println("-------------------------------------------------------");
        
    }
    
}
