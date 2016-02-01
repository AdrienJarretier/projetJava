package model;

import java.io.File;
import java.util.ArrayList;
import static org.eclipse.jgit.lib.ObjectChecker.type;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author COT
 */
public class Git {
    private String dossierGit;  
    ArrayList <GitObject> objects = new ArrayList();
    
    public void Git(String adresse){
        File dossierObjects= new File(adresse,"objects");
        for (File f: dossierObjects.listFiles()){
            for (File f2: f.listFiles()){
                type= Jgit.getType(f2);
                switch(type){
                case Blob:
                objects.add(new Blob(f2.getAbsolutePath()));
                break;
                case Tree:
                objects.add(new Tree());
                break;
                case Commit:
                objects.add(new Commit());
                break;
                case Tag:
                objects.add(new Tag());
                break;
            }
            }
}
