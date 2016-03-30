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
    
    
    
    private static ObjectType getType( File _gitObject ) throws IOException {
        
        Byte[] file = FileReading.ReadFile(_gitObject);
        StringBuilder mot = new StringBuilder();

        for (int i = 0; i < 10 && i < file.length; i++) {
            mot.append((char)file[i].byteValue());
        }


        if(mot.toString().startsWith("blob")){
            return ObjectType.BLOB;
        }

        else if(mot.toString().startsWith("tree")){
            return ObjectType.TREE;
        }

        else if(mot.toString().startsWith("commit")){
            return ObjectType.COMMIT;
        }


        else if(mot.toString().startsWith("tag")){
            return ObjectType.TAG;
        }
        
        else {
            return ObjectType.NONE;
        }
    }
    
    public Git(){
        gitDirectory = null;
        objects = new ArrayList();
    }
    
    public void setGitDirectory(File _gitDirectory) throws DirectoryDoesNotExistException, NotGitDirectoryException, IOException {
        
        if(!_gitDirectory.exists()) {
            throw new DirectoryDoesNotExistException("Le dossier <" + gitDirectory.getAbsolutePath() + "> n'existe pas");
        }
        
        if( !_gitDirectory.getName().equals(".git") ) {
            throw new NotGitDirectoryException("Le dossier <" + gitDirectory.getAbsolutePath() + "> n'est pas un dossier <.git>");
        }
        
        File objectsDirectory= new File(_gitDirectory,"objects");
        File tagsDirectory = new File(new File(_gitDirectory,"refs"),"tags");
        
        if(!objectsDirectory.exists()) {
            throw new DirectoryDoesNotExistException("Le dossier <" + objectsDirectory.getName()+ "> n'existe pas");
        }
        
        if(!tagsDirectory.exists()) {
            throw new DirectoryDoesNotExistException("Le dossier <refs/" + tagsDirectory.getName()+ "> n'existe pas");
        }
        
        gitDirectory = _gitDirectory;
        objects.clear();
        
        for (File f: objectsDirectory.listFiles()){

            // on ne traite pas les dossiers infos et pack pour le moment
            if(!f.getName().equals("pack") && !f.getName().equals("info") ) {     
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
                            objects.add(new Tag (f2));
                            break;
                    }
                }
            }
        }
        
//        for (File fTag : tagsDirectory.listFiles()) {
//            
//            objects.add( new Tag( fTag ) );
            
//        }
        
        setChanged();
        notifyObservers();
        
    }

    public ArrayList<GitObject> getObjects() {
        
        return objects;
        
    }
    
    // renvoie le git object associe a la cle de 40 caracteres
//    public GitObject find (String cle){
//        
//    }
    
}
    