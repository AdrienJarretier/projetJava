package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class AnnotedTag extends GitObject{
    
    private Commit referencedCommit;
    
    private String type;
    
    private String tagName;
    
    private String taggerName;
    private String taggerMail;
    private String dateTagged;
    
    private String message;
    
    public AnnotedTag(File _gitObjectFile, Git _gitInstance) throws IOException {
        
        super(_gitObjectFile, _gitInstance);
        
    }

    public AnnotedTag(String _name, Git _gitInstance, int offset, Pack pack) {
        
        super(_name, _gitInstance, offset, pack);
        
    }

    @Override
    protected void fill() throws IOException {
        
        if ( !this.filled ) {
            
            String content;

            if( inPack ) {
                
                content = FileReading.stringValue( this.pack.getRawDatas( this.offsetInPack ) );
                
            }
            else {
            
                content = FileReading.stringValue( FileReading.removeHeading( FileReading.ReadFile( this.getFile() ) ) );
                
            }

            StringReader sr = new StringReader( content );
            BufferedReader bf = new BufferedReader( sr );

            String line = bf.readLine();
            
            this.referencedCommit = (Commit)gitInstance.find( line.split(" ")[1] );
                   
            
            line = bf.readLine();
            
            this.type = line.split(" ")[1];
                 
            
            line = bf.readLine();
            
            this.tagName = line.split(" ")[1];
            
            
            line = bf.readLine();
            
            String[] wordsAuthorLine = line.split(" ");

            this.taggerName = wordsAuthorLine[1];

            int i=2;
            while( !wordsAuthorLine[i].startsWith("<") ) {

                this.taggerName += ' '+wordsAuthorLine[i];
                i++;
            }

            this.taggerMail = wordsAuthorLine[i++].replaceAll("[<>]", "");

            this.dateTagged = wordsAuthorLine[i++]+" "+wordsAuthorLine[i];
            
            line = bf.readLine();
            
            StringBuilder messageBuilder = new StringBuilder();
            while( line != null ) {

                messageBuilder.append( line ).append("\n");
                line = bf.readLine();
            }

            this.message = messageBuilder.toString();
            
            bf.close();
            sr.close();
            
            this.filled = true;
            
        }
        
    }

    @Override
    public ArrayList<GitObjectProperty> getProperties() throws IOException {
        
        this.fill();
        
        ArrayList<GitObjectProperty> properties = new ArrayList<>();
        
        
        properties.add( new GitObjectProperty( "referencedCommit" , GitObjectPropertyType.OBJECT_REF , referencedCommit ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "tagName" , GitObjectPropertyType.STRING , tagName ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "taggerName" , GitObjectPropertyType.STRING , taggerName ) );
        properties.add( new GitObjectProperty( "taggerMail" , GitObjectPropertyType.STRING , taggerMail ) );
        properties.add( new GitObjectProperty( "dateTagged" , GitObjectPropertyType.STRING , dateTagged ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "message" , GitObjectPropertyType.STRING_BLOC , message ) );
        
        return properties;
        
    }
    
}
