package model;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;



public class Commit extends GitObject{
    

    private String authorName;
    private String parent;
    private String committer;
    private String mailCommitter;
    private String date;
    private String mailAuthor;
    private String message;
   
    public Commit(File _file) throws IOException {
        
        super(_file);
        
//        System.out.println("-------------------------------------------------------");
//        System.out.println("nom : " + this.getName());
//        System.out.println(FileReading.stringValue( FileReading.ReadFile(_file) ));
//        System.out.println("-------------------------------------------------------");

    }   
}