package model;

import java.io.File;

/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class Pack {
    
    File idx;
    File pack;
    
    public Pack(File packFile) {
        
        File parent = packFile.getParentFile();
        
        this.idx = new File(parent, packFile.getName().replace(".pack", ".idx"));
        this.pack = packFile;
        
        System.out.println( "idx : " + this.idx.getName() );
        System.out.println( "pack : " + this.pack.getName() );
        
    }
    
}
