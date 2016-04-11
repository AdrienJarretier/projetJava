package model;

import exceptions.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.zip.InflaterInputStream;



public class Git extends Observable{
    private File gitDirectory;  
    private ArrayList<GitObject> objects;
    private GitObject selectedObject;
    
    // defini les types d'objets que l'on peut rencontrer dans .git/objects
    // cette enum correspond aux classes derivants de GitObject
    private enum ObjectType {
        BLOB, TREE, COMMIT, TAG,
        NONE
    }
    
    
    
    private static ObjectType getType( File _gitObject ) throws IOException {
        
        Byte[] file = FileReading.ReadFile(_gitObject);
        StringBuilder mot = new StringBuilder();
        
        // on recupere les 10 premiers caracteres du fichier qui correspondent
        // au type du fichier 
        for (int i = 0; i < 10 && i < file.length; i++) {
            mot.append((char)file[i].byteValue());
        }

        // puis on compare les caracteres obtenu aux differents noms des objets
        // git pour savoir le type d'objet qu'on a 
        
        if(mot.toString().startsWith("blob")){
            return ObjectType.BLOB;
        }

        else if(mot.toString().startsWith("tree")){
            return ObjectType.TREE;
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
        selectedObject = null;
    }

    
    public void setGitDirectory(File _gitDirectory) throws DirectoryDoesNotExistException, NotGitDirectoryException, IOException, Exception {
       // on gere les erreurs pouvant etre rencontrees avec le dossiers git  
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
        selectedObject = null;
        
        for (File f: objectsDirectory.listFiles()){

            // on ne traite pas le dossier infos
            // et le dossier pack est traite plus loin
            if(!f.getName().equals("pack") && !f.getName().equals("info") ) {
                for (File f2: f.listFiles()){

                    ObjectType type = getType(f2);

                    switch(type){

                        case BLOB:
                            objects.add(new Blob( f2, this ));
                            break;

                        case TREE:
                            objects.add(new Tree( f2, this ));
                            break;

                        case COMMIT:
                            objects.add(new Commit( f2, this ));
                            break;
                    }
                }
            }
            else if ( f.getName().equals("pack") ) {
                
                for (File f2: f.listFiles()){

                    if( f2.getName().endsWith(".pack") ) {
                        
                        Pack p = new Pack( f2, objects, this );
                        
                    }
                    
                }
                
            }
        }
        
        for (File fTag : tagsDirectory.listFiles()) {
            
            objects.add( new Tag( fTag, this ) );
            
        }
        
        setChanged();
        notifyObservers( new String("setGitDirectory") );
        
    }

    public ArrayList<GitObject> getObjects() {
        
        return objects;
        
    }
    
    // renvoie le git object associe a la cle de 40 caracteres
    public GitObject find (String cle){
        for (GitObject object : objects) {
            if (object.getName().equals(cle)) {
                return object;
            }
        }
        return null;
    }
    
    public void setSelectedObject( String cle ){
        
        this.selectedObject = this.find( cle ) ;
        
        setChanged();
        notifyObservers( "setSelectedObject" );
        
    }
    
    public GitObject getSelectObject() {
        
        return selectedObject;
        
    }
    
}
    