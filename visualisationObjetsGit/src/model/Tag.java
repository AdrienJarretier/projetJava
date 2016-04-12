package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Tag extends GitObject{
    
    private GitObject referencedCommit; // le commit reference par ce tag
    
    public Tag(File _file, Git _gitInstance) throws IOException {
        
        super(_file, _gitInstance);
        
        name = _file.getName();
        /*
            On ecrase le nom recupere dans super ( GitObject )
            car les tags sont dans le dossier refs/tags
            donc on ne veut pas concatener "tags"
        */
        
    }
    
    public Tag(String _name, Git _gitInstance, int offset, Pack pack) throws IOException {
        
        super(_name, _gitInstance, offset, pack);
        
    }

    @Override
    protected void fill() throws IOException {
        
        if ( !this.filled ) {
        
            FileReader fr = new FileReader( this.getFile() );
            BufferedReader bf = new BufferedReader(fr);
            
            referencedCommit = this.gitInstance.find( bf.readLine() );
            
            bf.close();
            fr.close();
            
            this.filled = true;
        }
    }

    @Override
    public ArrayList<GitObjectProperty> getProperties() throws IOException {
        
        this.fill();
        
        ArrayList<GitObjectProperty> properties = new ArrayList<>();
        properties.add(new GitObjectProperty( "ReferencedCommit", GitObjectPropertyType.OBJECT_REF, this.referencedCommit ));
        
        return properties;
    }
    
}
