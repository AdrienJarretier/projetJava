package model;

import java.io.BufferedReader;
import java.io.File;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;



public class Commit extends GitObject{
    
    private Tree tree;
    private ArrayList< Commit > parents;
    
    private String authorName;
    private String authorMail;
    private String dateWritten;
    
    private String committerName;
    private String committerMail;
    private String dateCommitted;
    
    private String message;
   
    public Commit(File _file, Git _gitInstance) {
        
        super(_file, _gitInstance);

    }
   
    public Commit(String _name, Git _gitInstance, int offset, Pack pack) {
        
        super(_name, _gitInstance, offset, pack);

    }
    
    @Override
    protected void fill() throws IOException { 
        
        if ( !this.filled ) {
        
            parents = new ArrayList<>();
            String content;

            if( inPack ) {
                
                content = FileReading.stringValue( this.pack.getRawDatas( this.offsetInPack ) );
                
            }
            else {
            
                content = FileReading.stringValue( FileReading.removeHeading( FileReading.ReadFile( this.getFile() ) ) );
                
            }
//            System.out.println(content);

            StringReader sr = new StringReader( content );
            BufferedReader bf = new BufferedReader( sr );

            String line = bf.readLine();

            tree = (Tree)gitInstance.find( line.split(" ")[1] );

            line = bf.readLine();

            int i;

            while( ! line.isEmpty() )
            {
                switch( line.charAt(0)) {

                    case 'p':
                        parents.add( (Commit)gitInstance.find( line.split(" ")[1] ) );
                        break;

                    case 'a':
                        String[] wordsAuthorLine = line.split(" ");

                        authorName = wordsAuthorLine[1];

                        i=2;
                        while( !wordsAuthorLine[i].startsWith("<") ) {

                            authorName += ' '+wordsAuthorLine[i];
                            i++;
                        }

                        authorMail = wordsAuthorLine[i++].replaceAll("[<>]", "");

                        dateWritten = wordsAuthorLine[i++]+" "+wordsAuthorLine[i];
                        break;

                    case 'c':
                        String[] wordsCommitterLine = line.split(" ");

                        committerName = wordsCommitterLine[1];

                        i=2;
                        while( !wordsCommitterLine[i].startsWith("<") ) {

                            committerName += ' '+wordsCommitterLine[i];
                            i++;
                        }

                        committerMail = wordsCommitterLine[i++].replaceAll("[<>]", "");

                        dateCommitted = wordsCommitterLine[i++]+" "+wordsCommitterLine[i];
                        break;    

                }

                line = bf.readLine();
            }

            StringBuilder messageBuilder = new StringBuilder();
            while( line != null ) {

                messageBuilder.append( line ).append("\n");
                line = bf.readLine();
            }

            message = messageBuilder.toString();
            
            this.filled = true;
            
        }
        
    }

    @Override
    public String toString() {
        
        try {

            this.fill();
            
            StringBuilder sb = new StringBuilder( "name : " + getName() );
            sb.append( System.getProperty("line.separator") );
            sb.append( System.getProperty("line.separator") );
            
            sb.append("tree : ");
            if( tree == null ) {
                sb.append( "null" );
            }
            else {
                sb.append(tree.getName());
            }
            sb.append( System.getProperty("line.separator") );
            sb.append( System.getProperty("line.separator") );
            
            parents.stream().forEach((parent) -> {
                sb.append("parent : ");
                if( parent == null ) {
                    sb.append( "null" );
                }
                else {
                    sb.append(parent.getName());
                }
                sb.append( System.getProperty("line.separator") );
            });
            sb.append( System.getProperty("line.separator") );
            
            sb.append("authorName : ").append(authorName);
            sb.append( System.getProperty("line.separator") );
            
            sb.append("authorMail : ").append(authorMail);
            sb.append( System.getProperty("line.separator") );
            
            sb.append("dateWritten : ").append(dateWritten);
            sb.append( System.getProperty("line.separator") );
            sb.append( System.getProperty("line.separator") );
            
            sb.append("committerName : ").append(committerName);
            sb.append( System.getProperty("line.separator") );
            
            sb.append("committerMail : ").append(committerMail);
            sb.append( System.getProperty("line.separator") );
            
            sb.append("dateCommitted : ").append(dateCommitted);
            sb.append( System.getProperty("line.separator") );
            sb.append( System.getProperty("line.separator") );
            
            sb.append("message : ").append(message);
            
            return sb.toString();

        } catch (IOException ex) {
            
            return ex.getMessage();
            
        }
    
    }

    @Override
    public ArrayList<GitObjectProperty> getProperties() throws IOException {
        
        this.fill();
        
        ArrayList<GitObjectProperty> properties = new ArrayList<>();
        
        properties.add( new GitObjectProperty( "tree" , GitObjectPropertyType.OBJECT_REF , tree ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
            
            parents.stream().forEach((parent) -> {
                properties.add( new GitObjectProperty( "parent" , GitObjectPropertyType.OBJECT_REF , parent ) );
            });
            
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "authorName" , GitObjectPropertyType.STRING , authorName ) );
        properties.add( new GitObjectProperty( "authorMail" , GitObjectPropertyType.STRING , authorMail ) );
        properties.add( new GitObjectProperty( "dateWritten" , GitObjectPropertyType.STRING , dateWritten ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "committerName" , GitObjectPropertyType.STRING , committerName ) );
        properties.add( new GitObjectProperty( "committerMail" , GitObjectPropertyType.STRING , committerMail ) );
        properties.add( new GitObjectProperty( "dateCommitted" , GitObjectPropertyType.STRING , dateCommitted ) );
        
        properties.add( new GitObjectProperty( "" , GitObjectPropertyType.BLOC_SEPARATOR , "" ) );
        
        properties.add( new GitObjectProperty( "message" , GitObjectPropertyType.STRING , message ) );
        
        return properties;
        
    }

}