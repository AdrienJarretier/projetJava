package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Git;
import model.GitObject;

/**
 * Vue de la liste des fichiers ".git/objects" sous forme de TreeView
 * 
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class GitObjectsFilesTreeView extends TreeView<String> implements Observer {
    
    private Git model;
    private TreeItem<String> rootTreeListeFichiers;
    private ArrayList< TreeItem<String> > objectsType;
    String[] types;
    
    /**
     * remplit la TreeView sur le cote gauche
     * avec la liste des fichiers de ".git/objects"
     * 
     * @param gitDirectory dossier ".git"
     * 
     */
    public void addObjectsFiles( File gitDirectory ) {
    
        // on vide la liste pour en refaire une nouvelle
        rootTreeListeFichiers.getChildren().clear();
        
        File gitObjectsDirectory = new File(gitDirectory, "objects");
        
        // on assigne le chemin "nomDepot/.git/objects" a l'item racine
            String[] pathDirectories = gitObjectsDirectory.getAbsolutePath().split( Pattern.quote(File.separator) );

            String rootValue = pathDirectories[pathDirectories.length-3];
            
            for (int i = pathDirectories.length-2; i < pathDirectories.length; i++) {
                rootValue += File.separator + pathDirectories[i];
            }
            rootTreeListeFichiers.setValue(rootValue);
        
        // liste des dossiers deroulee par defaut
        rootTreeListeFichiers.setExpanded(true);
        
        File[] gitObjectsSubDirectories = gitObjectsDirectory.listFiles();
        
        // pour chaque dossier dans objects on ajoute un item
        for (File gitObjectsSubDirectory : gitObjectsSubDirectories) {
            
            TreeItem<String> subDirectoryItem = new TreeItem<>(gitObjectsSubDirectory.getName());
            
            File[] gitObjects = gitObjectsSubDirectory.listFiles();
            
            // ajout de la liste des fichiers de ce dossier
            for (File gitObject : gitObjects) {
                TreeItem<String> objectItem = new TreeItem<>(gitObject.getName());
                subDirectoryItem.getChildren().add(objectItem);
            }
            
            rootTreeListeFichiers.getChildren().add(subDirectoryItem);
        }
        
    }
    
    /**
     * remplit la TreeView sur le cote gauche
     * avec la liste de GitObject du modele
     * 
     * @param objects liste des objets git
     * 
     */
    public void addListObjects( ArrayList<GitObject> objects ) {
    
        // on vide la liste pour en refaire une nouvelle
        for (TreeItem<String> itemObjectType : rootTreeListeFichiers.getChildren()) {
            
            itemObjectType.getChildren().clear();
            
        }

        for (GitObject object : objects) {
                
            for (int i = 0; i < this.types.length; i++) {
                
                if( this.types[i].contains( object.getClass().getSimpleName() ) ) {
            
                    TreeItem<String> item = new TreeItem<>( object.getName() );
                    objectsType.get(i).getChildren().add( item );
                }
                
            }
        }
        
        rootTreeListeFichiers.setExpanded(true);
        
    }
    
    public GitObjectsFilesTreeView( Git model ) {
    
        super();
    
        model.addObserver(this);
        
        this.model = model;
        
        rootTreeListeFichiers = new TreeItem("objets");

        // nouvelle TreeView avec l'element racine "rootTreeListeFichiers"
        // le dossier => ".git/objects"
        setRoot(rootTreeListeFichiers);
        
        setShowRoot(false);
        
        this.types = new String[]{"Blobs","Commits","Tags","Trees"};
        
        objectsType = new ArrayList<>(this.types.length);
        
        for (int i = 0; i < this.types.length; i++) {
            objectsType.add( new TreeItem<>( this.types[i] ) );
            rootTreeListeFichiers.getChildren().add( objectsType.get( i ) );
        }
        
    }

    @Override
    public void update(Observable o, Object arg) {
        
        addListObjects( model.getObjects() );
        
    }
}
