package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Tag extends GitObject{
    
    public Tag(File _file, Git _gitInstance) throws IOException {
        
        super(_file, _gitInstance);
        
        name = _file.getName();
        
        FileReader fr = new FileReader(_file);
        BufferedReader bf = new BufferedReader(fr);
        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("nom : " + this.getName());
//        System.out.println( bf.readLine() );
//        System.out.println("-------------------------------------------------------");
        
        
    }
    
}
