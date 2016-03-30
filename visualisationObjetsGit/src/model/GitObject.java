package model;

import java.io.File;


public abstract class GitObject {
    private File gitObjectFile;
    private String name;
    
    public File getFile(){
        return gitObjectFile;
    }
    
    public String getName(){
        return name;
    }
    
    public GitObject(File _gitObjectFile){
        
        gitObjectFile = _gitObjectFile;
        
        name = gitObjectFile.getParentFile().getName()+gitObjectFile.getName();
        
    }
    
}
