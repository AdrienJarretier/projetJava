package model;

import exceptions.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.zip.InflaterInputStream;



public class Git extends Observable{
    private File gitDirectory;  
    private ArrayList<GitObject> objects;
    
    // defini les types d'objets que l'on peut rencontrer dans .git/objects
    // cette enum correspond aux classes derivants de GitObject
    private enum ObjectType {
        BLOB, TREE, COMMIT, TAG,
        NONE
    }
    
    private static Byte[] ReadFile(File f) throws FileNotFoundException, IOException{
  				
	FileInputStream fis = new FileInputStream(f);
        
        InflaterInputStream decompresser = new InflaterInputStream(fis);
                
        ArrayList<Byte> LectureFichier = new ArrayList();
        int caract;
        
        try {
            while((caract = decompresser.read()) != -1){
                LectureFichier.add( (byte)caract );
            }
        }
        catch(IOException e) {
            throw new IOException("fichier "+f.getName()+" : "+e.getMessage());
        }
         
        return LectureFichier.toArray(new Byte[0]);         
               
    }
    
    
    
    private static ObjectType getType( File _gitObject ) throws IOException {
          Byte[] file = ReadFile(_gitObject);
          StringBuilder mot = new StringBuilder();
          for (int i = 0; i < 10; i++) {
             mot.append((char)file[i].byteValue());
                    }
          if(mot.toString().startsWith("tree")){
            return ObjectType.TREE;
          }
          
          else if(mot.toString().startsWith("tag")){
            return ObjectType.TAG;
          }
          
          else if(mot.toString().startsWith("blob")){
            return ObjectType.BLOB;
          }
          
          else if(mot.toString().startsWith("commit")){
            return ObjectType.COMMIT;
          }
          
          else {
              return ObjectType.NONE;
          }
    }
    
    public Git(){
        gitDirectory = null;
        objects = new ArrayList();
    }
    
    public void setGitDirectory(File _gitDirectory) throws DirectoryDoesNotExistException, NotGitDirectoryException, IOException{
        
        if(!_gitDirectory.exists()) {
            throw new DirectoryDoesNotExistException("Le dossier <" + gitDirectory.getAbsolutePath() + "> n'existe pas");
        }
        
        if( !_gitDirectory.getName().equals(".git") ) {
            throw new NotGitDirectoryException("Le dossier <" + gitDirectory.getAbsolutePath() + "> n'est pas un dossier <.git>");
        }
        
        File objectsDirectory= new File(_gitDirectory,"objects");
        
        if(!objectsDirectory.exists()) {
            throw new DirectoryDoesNotExistException("Le dossier <" + objectsDirectory.getName()+ "> n'existe pas");
        }
        
        gitDirectory = _gitDirectory;
        
        for (File f: objectsDirectory.listFiles()){

            // on ne traite pas les dossiers infos et pack pour le moment
            if(!f.getName().equals("pack") && f.getName().equals("info") ) {     
                for (File f2: f.listFiles()){

                    ObjectType type = getType(f2);

                    switch(type){

                        case BLOB:
                            objects.add(new Blob( f2 ));
                            break;

                        case TREE:
                            objects.add(new Tree( f2 ));
                            break;

                        case COMMIT:
                            objects.add(new Commit( f2 ));
                            break;

                        case TAG:
                            objects.add(new Tag( f2 ));
                            break;
                    }
                }
            }
        }
        
        setChanged();
        notifyObservers();
        
    }

    public File getGitDirectory() {
        
        return gitDirectory;
        
    }
    
}
    