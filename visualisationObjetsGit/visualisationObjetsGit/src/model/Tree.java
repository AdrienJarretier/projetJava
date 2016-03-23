package model;

import java.io.File;
import java.util.ArrayList;


public class Tree extends GitObject{
    
    private ArrayList<File> childs;
    
    public Tree(File _file) {
        
        super(_file);
        
    }
    
}
