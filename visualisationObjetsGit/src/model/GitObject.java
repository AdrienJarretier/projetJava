package model;

import java.io.File;


abstract class GitObject {
    private String content;
    private File gitObjectFile;
    
    public String getContent(){
        return content;
    }
    public String getAbsolutePath(){
        return gitObjectFile.getAbsolutePath();
    }
    public GitObject(File _gitObjectFile){
        
        gitObjectFile = _gitObjectFile;
        
    }
}
