package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class GitObject {
    private File gitObjectFile;
    protected String name;
    protected Git gitInstance;
    
    
    protected boolean inPack;
        // vrai si l'objet est dans le pack
    protected int offsetInPack;
        //  si inPack cet entier indique la position de l'objet dans le pack
    protected Pack pack;
        // si inPack represente le pack contenant cet objet
    
    protected boolean filled; 
        // permet de savoir si les proprietes de l'objet ont ete recuperees
        // c.a.d si fill() a ete appelee, methode qui parse le fichier
    
    public File getFile(){
        return gitObjectFile;
    }
    
    public String getName(){
        return name;
    }
    
    /**
     * Instancie un objet Git a partir d'un fichier
     * ce fichier se trouve dans un des dossiers de .git/objects
     * mais pas dans un pack
     * 
     * @param _gitObjectFile
     * @param _gitInstance
     */
    public GitObject(File _gitObjectFile, Git _gitInstance){
        
        gitObjectFile = _gitObjectFile;
        
        name = gitObjectFile.getParentFile().getName()+gitObjectFile.getName();
        
        gitInstance = _gitInstance;
        
        this.inPack = false;
        
        this.filled = false;
        
    }
    
    /**
     * Instancie un objet Git a partir d'un nom
     * Cet objet est suppose se trouver dans un pack
     * 
     * @param _name le nom de l'objet (40 caracteres >= hexadecimal)
     * @param _gitInstance l'instance Git contenant cet objet
     * @param offset la position de cet objet dans le pack
     * @param pack le pack ou se trouve cet objet
     */
    public GitObject(String _name, Git _gitInstance, int offset, Pack pack){
        
        gitObjectFile = null;
        
        name = _name;
        
        gitInstance = _gitInstance;
        
        
        this.inPack = true;
        this.offsetInPack = offset;
        this.pack = pack;
        
        
        this.filled = false;
        
    }
    
    protected abstract void fill() throws IOException;
    
    /**
     *
     * cette fonction permet de recuperer une liste des differents champs
     * presents dans ce GitObject
     * 
     * @see GitObjectProperty
     * 
     * @return la liste des proprietes de ce GitObject
     * @throws IOException
     */
    public abstract ArrayList<GitObjectProperty> getProperties() throws IOException;
    
}
