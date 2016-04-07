package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Blob extends GitObject{
    
    private Byte[] content;

    public Blob(File _file, Git _gitInstance) throws IOException {
        

        super(_file, _gitInstance);
        
    }

    @Override
    protected void fill() throws IOException {
        
        if ( !this.filled ) {
            
            content = FileReading.ReadFile( this.getFile() );
            
            this.filled = true;
        }
    }

    @Override
    public ArrayList<GitObjectProperty> getProperties() throws IOException {
        
        this.fill();
        
        ArrayList<GitObjectProperty> properties = new ArrayList<>();
        
        String stringContent = FileReading.stringValue(content);
        
        String sContentNoHeading = stringContent.split(" ")[1];

        properties.add( 
            new GitObjectProperty( 
                "content", 
                GitObjectPropertyType.STRING_BLOC, 
                sContentNoHeading
                ) 
            );
        
        return properties;
    }
    
}
